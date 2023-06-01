package com.micra.GOtur.controllers;

import com.micra.GOtur.mappers.*;
import com.micra.GOtur.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchaseGroup")
@CrossOrigin
public class PurchaseGroupController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PurchaseGroupController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/all")
    public List<PurchaseGroup> getAllPurchaseGroups() {
        String sql = "SELECT * FROM PurchaseGroup P;";

        List<PurchaseGroup> list = jdbcTemplate.query(sql, new PurchaseGroupMapper());

        return list;
    }

    @GetMapping("/{groupId}")
    public PurchaseGroup getPurchaseGroup(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM PurchaseGroup P WHERE P.group_id = ?;";

        return jdbcTemplate.queryForObject(sql, new PurchaseGroupMapper(), groupId);
    }

    @GetMapping("/allMembers/{groupId}")
    public List<Customer> getAllGroupMembers(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM Customer C, User U WHERE C.user_id = U.user_id AND C.user_id IN (SELECT F.group_member_id FROM Forms F WHERE F.group_id = ?)";
        List<Customer> list = jdbcTemplate.query(sql, new CustomerMapper(), groupId);

        return list;
    }

    @GetMapping("/groupOwner/{groupId}")
    public Customer getGroupOwnerByGroupId(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM Customer C, User U WHERE C.user_id = U.user_id AND C.user_id IN (SELECT P.group_owner_id FROM PurchaseGroup P WHERE P.group_id = ?)";
        return jdbcTemplate.queryForObject(sql, new CustomerMapper(), groupId);
    }

    @GetMapping("/allAddress/{groupId}")
    public List<Address> getAllAddressOfCustomer(@PathVariable("groupId") int groupId) {
        // Get the ID of the group owner
        String groupOwnerSql = "SELECT P.group_owner_id FROM PurchaseGroup P WHERE P.group_id = ?;";
        int groupOwnerId = jdbcTemplate.queryForObject(groupOwnerSql, Integer.class, groupId);

        // get addresses of the group owner
        String sql = "SELECT * FROM Address A WHERE A.customer_id = ?;";
        List<Address> list = jdbcTemplate.query(sql, new AddressMapper(), groupOwnerId);

        return list;
    }

    @GetMapping("/getPaidGroupPurchases/{groupId}")
    public List<Purchase> getAllPaidGroupPurchasesByGroupId(@PathVariable("groupId") int groupId) {
        String sql = "SELECT * FROM Purchase P WHERE P.is_group_purchase = 1 AND P.is_paid = 1 AND P.purchase_id IN (SELECT PG.purchase_id FROM PurchaseInGroup PG WHERE PG.group_id = ?);";
        List<Purchase> list = jdbcTemplate.query(sql, new PurchaseMapper(), groupId);

        return list;
    }

    @GetMapping("/getUnpaidGroupPurchase/{groupId}")
    public Purchase getUnpaidGroupPurchaseByPurchaseGroupId(@PathVariable("groupId") int groupId) {
        // check if the group has an unpaid purchase
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P NATURAL JOIN PurchaseInGroup PG  WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if ( !exists ) { // if the group does not have an unpaid single purchase
            return null;
        }

        // find the unpaid group purchase
        String findSql = "SELECT * FROM Purchase P WHERE P.is_group_purchase = 1 AND P.is_paid = 0 AND P.purchase_id IN (SELECT PG.purchase_id FROM PurchaseInGroup PG WHERE PG.group_id = ?);";
        return jdbcTemplate.queryForObject(findSql, new PurchaseMapper(), groupId);
    }

    @GetMapping("/getAllFoodAndIngredientsUnpaidGroupPurchase/{groupId}")
    public List<PurchaseItem> getAllFoodAndIngredientsForUnpaidGroupPurchaseByPurchaseGroupId(@PathVariable("groupId") int groupId) {
        // Check if the group exists
        String groupSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean existsGroup = jdbcTemplate.queryForObject(groupSql, Boolean.class, groupId);

        if (!existsGroup) { // if group does not exist
            return new ArrayList<>();
        }

        // check if the group already has an unpaid group purchase
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P NATURAL JOIN PurchaseInGroup PG  WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if ( !exists ) { // if the group does not have an unpaid purchase
            return new ArrayList<>();
        }

        // find the purchase id of an unpaid group purchase
        String purchaseSql = "SELECT P.purchase_id FROM Purchase P NATURAL  JOIN PurchaseInGroup PG WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0;";
        int purchaseId = jdbcTemplate.queryForObject(purchaseSql, Integer.class, groupId);

        String sql = "SELECT * FROM FoodInPurchase FP WHERE FP.purchase_id = ?;";
        List<FoodInPurchase> foodList = jdbcTemplate.query(sql, new FoodInPurchaseMapper(), purchaseId);
        List<PurchaseItem> purchaseItemList = new ArrayList<>();

        for (FoodInPurchase foodInPurchase: foodList) {
            int food_id = foodInPurchase.getFood_id();
            int food_order = foodInPurchase.getFood_order();

            String foodSql = "SELECT * FROM Food F WHERE F.food_id = ?;";
            Food food = jdbcTemplate.queryForObject(foodSql, new FoodMapper(), food_id);

            String ingredientSql = "SELECT * FROM Ingredient I WHERE I.food_id = ? AND I.ingredient_id IN (SELECT IP.ingredient_id FROM IngredientInPurchase IP WHERE IP.purchase_id = ? AND IP.food_order = ?);";
            List<Ingredient> ingredientList = jdbcTemplate.query(ingredientSql, new IngredientMapper(), food_id, purchaseId, food_order);

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setFood(food);
            purchaseItem.setIngredientList(ingredientList);
            purchaseItem.setFood_order(food_order);
            purchaseItemList.add(purchaseItem);
        }

        return purchaseItemList;
    }

    @GetMapping("/getAllPurchaseGroups/{customerId}")
    public List<PurchaseGroup> getAllPurchaseGroupsOfCustomerByCustomerId(@PathVariable("customerId") int customerId) {
        String sql = "SELECT * FROM PurchaseGroup PG WHERE PG.group_id IN (SELECT F.group_id FROM Forms F WHERE F.group_member_id = ?);";
        List<PurchaseGroup> list = jdbcTemplate.query(sql, new PurchaseGroupMapper(), customerId);

        return list;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPurchaseGroup(@RequestBody PurchaseGroup purchaseGroup) {

        SimpleJdbcInsert insertIntoGroup = new SimpleJdbcInsert(jdbcTemplate).withTableName("PurchaseGroup").usingGeneratedKeyColumns("group_id");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("group_owner_id", purchaseGroup.getGroup_owner_id());
        parameters.put("group_name", purchaseGroup.getGroup_name());
        parameters.put("group_balance", 0f);

        int groupId = insertIntoGroup.executeAndReturnKey(parameters).intValue();

        String sql1 = "INSERT INTO Forms(group_id, group_member_id) VALUES (?, ?);";

        System.out.println(">>" + sql1);
        jdbcTemplate.update(sql1, groupId, purchaseGroup.getGroup_owner_id());

        return new ResponseEntity<>("Purchase Group Successfully Inserted!", HttpStatus.OK);
    }

    @PostMapping("/add/{groupId}/{customerId}")
    public ResponseEntity<String> addCustomerToGroup(@PathVariable("groupId") int groupId, @PathVariable("customerId") int customerId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql1 = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, customerId);

        if (!exists1) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql2 = "SELECT EXISTS (SELECT * FROM PurchaseGroup P, Forms F WHERE P.group_id = F.group_id AND F.group_member_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, customerId);

        if (exists2) { // if the customer is already in the group
            return new ResponseEntity<>("Customer With ID: " + customerId + " already exists in the Purchase Group!", HttpStatus.BAD_REQUEST);
        }

        String sql = "INSERT INTO Forms(group_id, group_member_id) VALUES (?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, groupId, customerId);

        return new ResponseEntity<>("Customer is Successfully Inserted Into The Purchase Group!", HttpStatus.OK);
    }

    @PostMapping("/addAddress/{groupId}")
    public ResponseEntity<String> addAddressByPurchaseGroupId(@PathVariable("groupId") int groupId,
                                                              @RequestBody Address address) {
        // Check if the group exists
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // Get the ID of the group owner
        String groupOwnerSql = "SELECT P.group_owner_id FROM PurchaseGroup P WHERE P.group_id = ?;";
        int groupOwnerId = jdbcTemplate.queryForObject(groupOwnerSql, Integer.class, groupId);

        String sql = "INSERT INTO Address(customer_id, address_name, is_primary, city, district, street_num, street_name, building_num, detailed_desc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, groupOwnerId, address.getAddress_name(), address.getIs_primary(), address.getCity(), address.getDistrict(), address.getStreet_num(),
                address.getStreet_name(), address.getBuilding_num(), address.getDetailed_desc());

        return new ResponseEntity<>("Address Is Successfully Added To The Purchase Group!", HttpStatus.OK);
    }

    @PostMapping("/transferToGroupBalance/{groupId}/{customerId}")
    public ResponseEntity<String> transferBalanceFromGroupMemberToGroupBalance(@PathVariable("groupId") int groupId,
                                                                               @PathVariable("customerId") int customerId,
                                                                               @RequestParam int transferAmount) {
        // Check if the group exists
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // Check if the group exists
        String memberCheckSql = "SELECT EXISTS (SELECT * FROM Forms F WHERE F.group_id = ? AND F.group_member_id = ?);";
        boolean existsMember = jdbcTemplate.queryForObject(memberCheckSql, Boolean.class, groupId, customerId);

        if (!existsMember) { // if group member does not belong
            return new ResponseEntity<>("Customer With ID: " + customerId + " Is Not Member Of The Purchase Group With ID: " + groupId + "!", HttpStatus.BAD_REQUEST);
        }

        // check if the customer has enough balance
        String checkBalanceSql = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ? AND C.balance >= ?);";
        boolean isEnoughBalance = jdbcTemplate.queryForObject(checkBalanceSql, Boolean.class, customerId, transferAmount);

        if ( !isEnoughBalance ) {
            return new ResponseEntity<>("The Balance Of The Customer Is Not Enough To Complete This Transaction!", HttpStatus.BAD_REQUEST);
        }

        // Update the customer balance
        String customerSql = "UPDATE Customer C SET C.balance = C.balance - ? WHERE C.user_id = ?;";
        System.out.println(">>" + customerSql);
        jdbcTemplate.update(customerSql, transferAmount, customerId);

        // Update the group balance
        String groupSql = "UPDATE PurchaseGroup P SET P.group_balance = P.group_balance + ? WHERE P.group_id = ?;";
        System.out.println(">>" + groupSql);
        jdbcTemplate.update(groupSql, transferAmount, groupId);

        return new ResponseEntity<>("The Transaction Has Been Completed Successfully!", HttpStatus.OK);
    }

    @PostMapping("/addFoodWithIngredient/{groupId}")
    public ResponseEntity<String> addPurchaseItemToPurchase(@PathVariable("groupId") int groupId,
                                                            @RequestBody PurchaseItem purchaseItem) {
        // Check if the group exists
        String groupSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean existsGroup = jdbcTemplate.queryForObject(groupSql, Boolean.class, groupId);

        if (!existsGroup) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        Food food = purchaseItem.getFood();
        List<Ingredient> ingredientList = purchaseItem.getIngredientList();
        int restaurant_id = food.getRestaurant_id();

        // check if the group already has an unpaid group purchase
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P NATURAL JOIN PurchaseInGroup PG  WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        int purchase_id = -1; // will be changed separately in the blocks
        if (exists) { // the group already has an unpaid purchase, check if the restaurant names match
            String restaurantSql = "SELECT P.restaurant_id FROM Purchase P NATURAL JOIN PurchaseInGroup PG WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0;";
            int existing_restaurant_id = jdbcTemplate.queryForObject(restaurantSql, Integer.class, groupId);

            if ( restaurant_id != existing_restaurant_id ) {
                return new ResponseEntity<>("Purchase Group With ID: " + groupId + " Already Has An Unpaid Purchase From Another Restaurant!", HttpStatus.BAD_REQUEST);
            }

            String purchaseSql = "SELECT P.purchase_id FROM Purchase P NATURAL  JOIN PurchaseInGroup PG WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0;";
            purchase_id = jdbcTemplate.queryForObject(purchaseSql, Integer.class, groupId);
        }
        else { // insert a new purchase
            // Get the id of the group owner
            String ownerSql = "SELECT P.group_owner_id FROM PurchaseGroup P WHERE P.group_id = ?;";
            int group_owner_id = jdbcTemplate.queryForObject(ownerSql, Integer.class, groupId);

            SimpleJdbcInsert insertIntoRestaurant = new SimpleJdbcInsert(jdbcTemplate).withTableName("Purchase").usingColumns("customer_id", "restaurant_id", "is_group_purchase").usingGeneratedKeyColumns("purchase_id");
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("customer_id", group_owner_id);
            parameters.put("restaurant_id", restaurant_id);
            parameters.put("is_group_purchase", Boolean.TRUE);

            // Get the inserted id back
            purchase_id = insertIntoRestaurant.executeAndReturnKey(parameters).intValue();

            // insert the purchase to PurchaseInGroup table
            String insertGroupPurchase = "INSERT INTO PurchaseInGroup(purchase_id, group_id) VALUES (?, ?);";
            System.out.println(">>" + insertGroupPurchase);
            jdbcTemplate.update(insertGroupPurchase, purchase_id, groupId);
        }

        // get the number of foods with that specific food_id
        int food_id = food.getFood_id();
        String checkFoodSql = "SELECT COUNT(*) FROM FoodInPurchase F WHERE F.purchase_id = ? AND F.food_id = ?;";
        int foodCount = jdbcTemplate.queryForObject(checkFoodSql, Integer.class, purchase_id, food_id);

        // insert the food to FoodInPurchase
        String insertFood = "INSERT INTO FoodInPurchase(food_id, purchase_id, food_order) VALUES (?, ?, ?);";
        System.out.println(">>" + insertFood);
        jdbcTemplate.update(insertFood, food_id, purchase_id, foodCount + 1);

        for (Ingredient ingredient : ingredientList) {
            // insert the ingredient to IngredientInPurchase
            int ingredient_id = ingredient.getIngredient_id();
            String insertIngredient = "INSERT INTO IngredientInPurchase(ingredient_id, purchase_id, food_order) VALUES (?, ?, ?);";
            System.out.println(">>" + insertIngredient);
            jdbcTemplate.update(insertIngredient, ingredient_id, purchase_id, foodCount + 1);
        }

        return new ResponseEntity<>("Purchase Has Been Successfully Formed!", HttpStatus.OK);
    }

    @PostMapping("/completeGroupPurchase/{purchaseId}")
    public ResponseEntity<String> completeGroupPurchaseByPurchaseId(@PathVariable("purchaseId") int purchaseId,
                                                               @RequestParam int addressId,
                                                               @RequestParam String customerNote,
                                                               @RequestParam int couponId) {
        // check if the group already has an unpaid group purchase
        String checkGroupSql = "SELECT EXISTS (SELECT * FROM PurchaseInGroup PG  WHERE PG.purchase_id = ?);";
        boolean existsGroupPurchase = jdbcTemplate.queryForObject(checkGroupSql, Boolean.class, purchaseId);

        if ( !existsGroupPurchase ) { // the purchase is not a group purchase
            return new ResponseEntity<>("The Purchase With ID: " + purchaseId + " Is Not A Group Purchase!", HttpStatus.BAD_REQUEST);
        }

        // get the group id
        String groupIdSql = "SELECT PG.group_id FROM PurchaseInGroup PG  WHERE PG.purchase_id = ?;";
        int groupId = jdbcTemplate.queryForObject(groupIdSql, Integer.class, purchaseId);

        // check if the group has an unpaid group purchase
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P NATURAL JOIN PurchaseInGroup PG  WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if ( !exists ) {
            return new ResponseEntity<>("There Is No Unpaid Single Purchase With ID: " + purchaseId + "!", HttpStatus.BAD_REQUEST);
        }

        // get the balance of the group
        String getBalanceSql = "SELECT PG.group_balance FROM PurchaseGroup PG WHERE PG.group_id = ?;";
        float group_balance = jdbcTemplate.queryForObject(getBalanceSql, Float.class, groupId);

        // get the total price of the purchase
        String getTotalPriceSql = "SELECT P.total_price FROM Purchase P WHERE P.purchase_id = ?;";
        float total_price = jdbcTemplate.queryForObject(getTotalPriceSql, Float.class, purchaseId);

        if ( group_balance < total_price ) { // if the group balance is not enough
            return new ResponseEntity<>("The Balance Of The Group Is Not Enough To Complete This Transaction!", HttpStatus.BAD_REQUEST);
        }

        // get the min delivery price of the restaurant
        String getMinDeliveryPriceSql = "SELECT R.min_delivery_price FROM Purchase P NATURAL JOIN Restaurant R WHERE P.purchase_id = ?;";
        int min_delivery_price = jdbcTemplate.queryForObject(getMinDeliveryPriceSql, Integer.class, purchaseId);

        if ( total_price < min_delivery_price ) { // if the total price is less than the min delivery
            return new ResponseEntity<>("The Total Price Of The Purchase: " + total_price + " Is Less Than The Minimum Delivery Price Of The Restaurant!", HttpStatus.BAD_REQUEST);
        }

        // check if the discount coupon is active and exists
        String checkCouponSql = "SELECT EXISTS (SELECT * FROM DiscountCoupon D WHERE D.coupon_id = ? AND D.is_used = 0);";
        boolean existsCoupon = jdbcTemplate.queryForObject(checkCouponSql, Boolean.class, couponId);

        if ( existsCoupon ) { // if coupon is valid, get the discount percentage of the restaurant and apply
            String getDiscountSql = "SELECT R.discount_percentage FROM Purchase P NATURAL JOIN Restaurant R WHERE P.purchase_id = ?;";
            float discount_percentage = jdbcTemplate.queryForObject(getDiscountSql, Integer.class, purchaseId);

            total_price = (total_price * (100 - discount_percentage)) / 100; // get the discounted total price

            // Update the discount coupon to used
            String sql = "UPDATE DiscountCoupon D SET D.is_used = 1 WHERE D.coupon_id = ?;";
            jdbcTemplate.update(sql, couponId);
        }

        // Update the purchase
        String sql = "UPDATE Purchase P SET P.address_id = ?, P.customer_note = ?, P.is_paid = 1, P.being_prepared = 1, P.purchase_time = ? WHERE P.purchase_id = ?;";
        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, addressId, customerNote, LocalDateTime.now(), purchaseId);

        // get the updated purchase
        String getPurchaseSql = "SELECT * FROM Purchase P WHERE P.purchase_id = ?;";
        Purchase purchase = jdbcTemplate.queryForObject(getPurchaseSql, new PurchaseMapper(), purchaseId);

        // Update the group balance
        String groupSql = "UPDATE PurchaseGroup PG SET PG.group_balance = PG.group_balance - ? WHERE PG.group_id = ?;";
        System.out.println(">>" + groupSql);
        jdbcTemplate.update(groupSql, total_price, groupId);

        // Update the restaurant balance
        String restaurantSql = "UPDATE Restaurant R SET R.total_earnings = R.total_earnings + ? WHERE R.restaurant_id = ?;";
        System.out.println(">>" + restaurantSql);
        jdbcTemplate.update(restaurantSql, total_price, purchase.getRestaurant_id());

        return new ResponseEntity<>("The Purchase Has Been Completed Successfully!", HttpStatus.OK);
    }

    @PostMapping("/cancelGroupPurchase/{purchaseId}")
    public ResponseEntity<String> cancelSinglePurchaseByPurchaseId(@PathVariable("purchaseId") int purchaseId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_group_purchase = 1);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, purchaseId);

        if (!exists) { // if single purchase does not exist
            return new ResponseEntity<>("Group Purchase With ID: " + purchaseId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        if (!IsPaidByPurchaseId(purchaseId)) {
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Not Paid! Nothing To Cancel!", HttpStatus.BAD_REQUEST);
        }

        // check if already delivered
        String checkStatusSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_delivered = 1);";
        boolean existsStatus = jdbcTemplate.queryForObject(checkStatusSql, Boolean.class, purchaseId);

        if (existsStatus) { // if status is already delivered
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Already Delivered And Cannot Be Canceled! ", HttpStatus.BAD_REQUEST);
        }

        // check if already canceled
        String checkCanceledStatusSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_canceled = 1);";
        boolean canceledStatus = jdbcTemplate.queryForObject(checkCanceledStatusSql, Boolean.class, purchaseId);

        if (canceledStatus) { // if status is already delivered
            return new ResponseEntity<>("Purchase With ID: " + purchaseId + " Is Already Canceled! ", HttpStatus.BAD_REQUEST);
        }

        // Update the status
        String sql = "UPDATE Purchase P SET P.is_canceled = 1 WHERE P.purchase_id = ?;";
        jdbcTemplate.update(sql, purchaseId);

        // get the updated purchase
        String getPurchaseSql = "SELECT * FROM Purchase P WHERE P.purchase_id = ?;";
        Purchase purchase = jdbcTemplate.queryForObject(getPurchaseSql, new PurchaseMapper(), purchaseId);

        // Update the group balance
        String customerSql = "UPDATE PurchaseGroup P SET P.group_balance = P.group_balance + ? WHERE P.group_id IN (SELECT PG.group_id FROM PurchaseInGroup PG WHERE PG.purchase_id = ?);";
        System.out.println(">>" + customerSql);
        jdbcTemplate.update(customerSql, purchase.getTotal_price(), purchaseId);

        // Update the restaurant balance
        String restaurantSql = "UPDATE Restaurant R SET R.total_earnings = R.total_earnings - ? WHERE R.restaurant_id = ?;";
        System.out.println(">>" + restaurantSql);
        jdbcTemplate.update(restaurantSql, purchase.getTotal_price(), purchase.getRestaurant_id());

        return new ResponseEntity<>("The Status Of The Purchase With ID: " + purchaseId + " Has Been Successfully Updated To Canceled!", HttpStatus.OK);
    }

    public Boolean IsPaidByPurchaseId(int purchaseId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P WHERE P.purchase_id = ? AND P.is_paid = 1);";
        return jdbcTemplate.queryForObject(checkSql, Boolean.class, purchaseId);
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<String> deletePurchaseGroup(@PathVariable("groupId") int groupId) {

        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) {
            return new ResponseEntity<>("The Purchase Group with ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM PurchaseGroup P WHERE P.group_id = ?;";
        jdbcTemplate.update(sql, groupId);

        return new ResponseEntity<>("Purchase Group With ID: " + groupId + " has been deleted!",
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{groupId}/{customerId}")
    public ResponseEntity<String> deleteCustomerFromGrouo(@PathVariable("groupId") int groupId, @PathVariable("customerId") int customerId) {
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if group does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql1 = "SELECT EXISTS (SELECT * FROM Customer C WHERE C.user_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, customerId);

        if (!exists1) { // if customer does not exist
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        String checkSql2 = "SELECT EXISTS (SELECT * FROM PurchaseGroup P, Forms F WHERE P.group_id = F.group_id AND F.group_member_id = ?);";
        boolean exists2 = jdbcTemplate.queryForObject(checkSql2, Boolean.class, customerId);

        if (!exists2) { // if the customer is already in the group
            return new ResponseEntity<>("Customer With ID: " + customerId + " does not exist in the Purchase Group!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM Forms F WHERE F.group_id = ? AND F.group_member_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, groupId, customerId);

        return new ResponseEntity<>("Customer is Successfully Deleted From The Purchase Group!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAddress/{groupId}/{addressId}")
    public ResponseEntity<String> deleteAddressFromPurchaseGroup(@PathVariable("groupId") int groupId,
                                                            @PathVariable("addressId") int addressId) {
        // Check if the group exists
        String checkSql = "SELECT EXISTS (SELECT * FROM PurchaseGroup P WHERE P.group_id = ?);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if (!exists) { // if customer does not exist
            return new ResponseEntity<>("Purchase Group With ID: " + groupId + " does not exist!", HttpStatus.BAD_REQUEST);
        }

        // Get the ID of the group owner
        String groupOwnerSql = "SELECT P.group_owner_id FROM PurchaseGroup P WHERE P.group_id = ?;";
        int groupOwnerId = jdbcTemplate.queryForObject(groupOwnerSql, Integer.class, groupId);

        // Check if the address exists
        String checkSql1 = "SELECT EXISTS (SELECT * FROM Address A WHERE A.address_id = ? AND A.customer_id = ?);";
        boolean exists1 = jdbcTemplate.queryForObject(checkSql1, Boolean.class, addressId, groupOwnerId);

        if (!exists1) { // such address does not exist!
            return new ResponseEntity<>("Address With ID: " + addressId + " does not exist in Purchase Group With ID: " + groupId + "!", HttpStatus.BAD_REQUEST);
        }

        String sql = "DELETE FROM Address A WHERE A.address_id = ?;";

        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, addressId);

        return new ResponseEntity<>("Address Is Successfully Deleted From The Purchase Group!", HttpStatus.OK);
    }

    @DeleteMapping("/deleteFoodWithIngredientFromGroupUnpaidPurchase/{groupId}/{foodId}/{foodOrder}")
    public ResponseEntity<String> deleteFoodAndIngredientsFromGroupUnpaidPurchaseByFoodIdAndFoodOrder(@PathVariable("groupId") int groupId,
                                                                                                       @PathVariable("foodId") int foodId,
                                                                                                       @PathVariable("foodOrder") int foodOrder) {
        // check if the group already has an unpaid group purchase
        String checkSql = "SELECT EXISTS (SELECT * FROM Purchase P NATURAL JOIN PurchaseInGroup PG  WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0);";
        boolean exists = jdbcTemplate.queryForObject(checkSql, Boolean.class, groupId);

        if ( !exists ) { // if the customer does not have an unpaid single purchase
            return new ResponseEntity<>("The Purchase Group With ID: " + groupId + " Does Not Currently Have An Unpaid Purchase!", HttpStatus.BAD_REQUEST);
        }

        // find the id of the unpaid group purchase
        String purchaseSql = "SELECT P.purchase_id FROM Purchase P NATURAL  JOIN PurchaseInGroup PG WHERE PG.group_id = ? AND P.is_group_purchase = 1 AND P.is_paid = 0;";
        int purchaseId = jdbcTemplate.queryForObject(purchaseSql, Integer.class, groupId);

        // delete the ingredients
        String sql = "DELETE FROM IngredientInPurchase IP WHERE IP.purchase_id = ? AND IP.food_order = ? AND IP.ingredient_id IN (SELECT I.ingredient_id FROM Ingredient I WHERE I.food_id = ?);";
        System.out.println(">>" + sql);
        jdbcTemplate.update(sql, purchaseId, foodOrder, foodId);

        // delete the food
        String foodSql = "DELETE FROM FoodInPurchase FP WHERE FP.purchase_id = ? AND FP.food_order = ? AND FP.food_id = ?;";
        System.out.println(">>" + foodSql);
        jdbcTemplate.update(foodSql, purchaseId, foodOrder, foodId);

        // if there's no other item left, delete the purchase
        String countSql = "SELECT COUNT(*) FROM FoodInPurchase FP WHERE FP.purchase_id = ?;";
        int foodCount = jdbcTemplate.queryForObject(countSql, Integer.class, purchaseId);

        if ( foodCount == 0 ) {
            String deletePurchaseSql = "DELETE FROM Purchase P WHERE P.purchase_id = ?;";
            System.out.println(">>" + deletePurchaseSql);
            jdbcTemplate.update(deletePurchaseSql, purchaseId);
        }

        return new ResponseEntity<>("Selected Food And Ingredients Has Been Successfully Deleted From The Purchase List Of The Purchase Group With ID: " + groupId + "!", HttpStatus.OK);
    }
}
