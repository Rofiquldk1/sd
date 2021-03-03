package com.agamilabs.smartshop.constants;

public class AppServerURL {
    private static final boolean LOCAL = false;

    //public static final String ROOT_URL = LOCAL?"http://192.168.1.123/aamiri/":"http://aamiri.agamilabs.com/";

    public static final String ROOT_URL = "http://batikrom.shop/v2/batikromapp/php/ui/";
    public static final String IMAGE_ROOT_URL = "http://batikrom.shop/v2/";

    public static final String STORE_IMAGE_ROOT_URL = "http://betikrom.agamilab.com/v2/assets/images/storeimages/";
    public static final String STORE_PRODUCT_IMAGE_ROOT_URL = "http://betikrom.agamilab.com/v2/assets/images/product/";
    public static final String SYSTEM_PRODUCT_IMAGE_ROOT_URL = "http://betikrom.agamilab.com/v2/assets/images/systemproduct/";

    public static final String GET_APPS = "http://agamilabs.com/apis/php/products/get_all_live_products_of_a_category.php";

    public static final String LOGIN_USER = ROOT_URL + "usermodule/user/login_user.php";

    public static final String Face_URL = ROOT_URL + "getFaceInfo.php";
    public static final String Eye_URL = ROOT_URL + "getEyeInfo.php";
    public static final String Lip_URL = ROOT_URL + "getLipInfo.php";
    public static final String HairCare_URL = ROOT_URL + "getHairCareInfo.php";
    public static final String Perfume_URL = ROOT_URL + "getPerfumeInfo.php";

    //public static final String GET_COVER_SLIDER = ROOT_URL + "slider/get_masterslider.php";

    public static class UserModule {
        private static final String MODULE_ROOT = "usermodule/";

        public static final String SLIDER_REQUEST_TYPE = "slider_request_type";

        public static class SLIDERTYPES {
            public static final String USER_LANDING_PAGE = "user_landing_page";
            public static final String MASTER_SLIDER = "master_slider";
        }

        public static class Bid {
            public static final String BID_ROOT_URL = "http://batikrom.shop/v2/bid/";
            public static final String MY_BID = BID_ROOT_URL + "mybid.php?";
        }

        public static class LandingPage {
            public static final String GET_SLIDER = ROOT_URL + UserModule.MODULE_ROOT + "sliders/get_slider.php"; // verified; input slider type    // check -> ok
        }

        public static class User {
            public static final String REQUEST_CHANGE_PASSWORD = ROOT_URL + UserModule.MODULE_ROOT + "user/request_change_password.php";
            public static final String CHANGE_PASSWORD = ROOT_URL + UserModule.MODULE_ROOT + "user/changepassword_mobile.php";
        }

        // for rating and review
        public static class CartRatingReview {
            public static final String ADD_A_CART_REVIEW = ROOT_URL + UserModule.MODULE_ROOT + "rating_review/add_rating_review_of_a_user.php";
            public static final String GET_ALL_RATING_REVIEW_OF_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "rating_review/get_all_filtered_reviews_of_a_cart.php";
        }

        public static class ComplaintUrl {
            public static final String ADD_A_COMPLAINT = ROOT_URL + UserModule.MODULE_ROOT + "complaint/add_complaint_of_a_user.php";
            public static final String GET_FILTERED_COMPLAINTS = ROOT_URL + UserModule.MODULE_ROOT + "complaint/get_all_filtered_complaints.php";
            public static final String GET_ALL_COMPLAINT_TAGS = ROOT_URL + UserModule.MODULE_ROOT + "complaint/get_all_complaint_tags.php";
            public static final String GET_ALL_REPLIES_OF_A_COMPLAINT = ROOT_URL + UserModule.MODULE_ROOT + "complaint/get_all_replies_of_a_complaint.php";
        }

        public static class StoreMessage {
            public static final String INSERT_A_STORE_MESSAGE = ROOT_URL + UserModule.MODULE_ROOT + "messages/insert_a_message_from_customer.php"; // userno, storeno, message
            public static final String GET_A_STORE_MESSAGE = ROOT_URL + UserModule.MODULE_ROOT + "messages/get_messages_of_a_store.php"; // userno, storeno, pageno
            public static final String GET_A_STORE_MESSAGE_BY_LAST_MSG_ID = ROOT_URL + UserModule.MODULE_ROOT + "messages/get_messages_by_lastslno.php"; // userno, storeno, lastslno
        }

        public static class Shop {
            public static final String GET_SHOP_CATEGORY_MASTER_SLIDER = ROOT_URL + UserModule.MODULE_ROOT + "store/get_shop_category_master_slider.php";
            public static final String GET_CATEGORY_PAGE_DISPLAY = ROOT_URL + UserModule.MODULE_ROOT + "category/get_category_page_display.php"; // check -> ok
            public static final String GET_CATEGORY_DISPLAY_BLOCK = ROOT_URL + UserModule.MODULE_ROOT + "category/get_category_display_block.php"; // check -> ok
            public static final String GET_SUBCATEGORY_OF_A_CATEGORY = ROOT_URL + UserModule.MODULE_ROOT + "category/get_subcategory_of_a_category.php"; // check -> ok
        }

        public static class Product {
            public static final String GET_DISCOUNT_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "product/get_discount_products.php"; // check -> ok
            public static final String GET_NEW_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "product/get_new_products.php"; // check -> ok
            public static final String GET_DEMANDED_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "product/get_demanded_products.php"; // check -> ok
            public static final String GET_FAVOURITE_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "product/get_favourite_products.php"; // check -> ok

            public static final String GET_RECOMMENDED_PRODUCTS = ROOT_URL + UserModule.MODULE_ROOT + "product/get_recommended_products.php"; // storeno, productno

            public static final String GET_ALL_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "product/get_all_product.php"; //for product search key


            public static final String GET_LANDING_PAGE_PRODUCTS = ROOT_URL + UserModule.MODULE_ROOT + "product/get_landing_page_products.php"; //for product search key
        }

        public static class WishList {
            public static final String GET_FILTERED_WISHLIST = ROOT_URL + UserModule.MODULE_ROOT + "wishlist/get_filtered_wishlist.php";
            public static final String ADD_A_PRODUCT_TO_WISHLIST = ROOT_URL + UserModule.MODULE_ROOT + "wishlist/insert_a_storeproduct_to_wishlist.php";
            public static final String REMOVE_A_PRODUCT_FROM_WISHLIST = ROOT_URL + UserModule.MODULE_ROOT + "wishlist/delete_a_product_from_wishlist.php";
            public static final String GET_STORE_PRODUCT_DETAILS_FROM_WISHLIST = ROOT_URL + UserModule.MODULE_ROOT + "wishlist/get_store_product_details.php";
            public static final String CHECK_IF_A_PRODUCT_IS_IN_WISHLIST = ROOT_URL + UserModule.MODULE_ROOT + "wishlist/check_if_a_storeproduct_is_in_wishlist.php";
        }

        public static class Store {
            public static final String STORE_CATEGORY_PAGE_DISPLAY_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "store/get_category_page_display_products.php";
            public static final String STORE_MASTER_SLIDER = ROOT_URL + UserModule.MODULE_ROOT + "store/get_store_master_slider.php";
            public static final String STORE_LIST_OF_SELECTED_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "store/get_product_from_different_stores.php";
            public static final String STORE_PRODUCT_DETAILS = ROOT_URL + UserModule.MODULE_ROOT + "store/get_store_product_details.php";
            public static final String GET_NEAR_STORES_IN_RANGE = ROOT_URL + UserModule.MODULE_ROOT + "store/get_stores_in_range_near_user.php"; // check -> ok
            public static final String GET_STORE_AND_PRODUCT_WITH_SEARCH_KEY = ROOT_URL + UserModule.MODULE_ROOT + "store/get_store_and_product_with_name.php";


            public static final String STORE_PAGE_DISPLAY_TYPE = ROOT_URL + UserModule.MODULE_ROOT + "store/get_store_page_display_types.php";//storeno
            public static final String GET_STORE_PAGE_DISPLAY_BLOCK = ROOT_URL + UserModule.MODULE_ROOT + "store/get_store_page_display_block.php"; // userno, typeno, storeno
            public static final String GET_STORE_INFO = ROOT_URL + UserModule.MODULE_ROOT + "store/get_a_store_info.php"; // storeno, userno

            public static final String GET_ALL_STORE_INFO = ROOT_URL + UserModule.MODULE_ROOT + "store/get_all_store_info.php"; //for store search key
        }

        public static class Category {
            public static final String CATEGORY_HIERARCHY_URL_ORG = ROOT_URL + UserModule.MODULE_ROOT + "category/get_category_hierarchy.php";
            public static final String CATEGORY_HIERARCHY_URL = ROOT_URL + UserModule.MODULE_ROOT + "category/get_category_hierarchy_web.php";
            public static final String CATEGORY_HIERARCHY_URL_MINIFIED = ROOT_URL + UserModule.MODULE_ROOT + "category/get_category_hierarchy_minified.php"; // check -> ok
            public static final String CATEGORY_HIERARCHY_URL_WEB = "http://aamiribeta.agamilabs.com/user/php/ui/category/get_category_hierarchy_web.php";
            public static final String GET_SELECTED_ITEM_URL = ROOT_URL + UserModule.MODULE_ROOT + "category/get_products_of_a_category.php";
            public static final String SELECTED_PRODUCT_OF_A_CATEGORY_OF_A_STORE = ROOT_URL + UserModule.MODULE_ROOT + "category/get_selected_products_of_a_category_of_a_store.php";
            public static final String CATEGORY_HIERARCHY_STORE_URL = ROOT_URL + UserModule.MODULE_ROOT + "category/get_category_hierarchy_store.php";
            public static final String GET_CATEGORY_HIERARCHY_USER_PRODUCT = ROOT_URL + UserModule.MODULE_ROOT + "category/get_category_hierarchy_for_user_product.php";
        }

        public static class Cart {
            public static final String GET_ALL_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/get_all_filtered_carts_of_an_user.php";
            //public static final String GET_A_CART_DETAIL = ROOT_URL + "cart/get_detail_of_a_cart.php";
            //public static final String GET_A_CART_DETAIL = ROOT_URL + "cart/get_cart_detail_of_a_user.php";
            public static final String GET_STORE_WISE_CARTS = ROOT_URL + UserModule.MODULE_ROOT + "cart/get_store_wise_carts_of_a_user.php";
            public static final String CONFIRM_AN_ORDER = ROOT_URL + UserModule.MODULE_ROOT + "cart/confirm_an_order_by_a_user.php";
            public static final String ORDER_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/order_a_cart_by_a_user_mobile.php";
            public static final String RECEIVE_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/receiving_a_cart_by_a_user.php";
            public static final String REJECT_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/reject_an_order_by_a_user.php";
            public static final String ADD_PRODUCT_TO_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/add_product_to_a_cart.php";
            public static final String UPDATE_QUANTITY_OF_A_STORE_PRODUCT_IN_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/update_qty_of_a_product_in_a_cart.php";
            public static final String REMOVE_A_PRODUCT_FROM_A_STORE_CART_OF_A_USER = ROOT_URL + UserModule.MODULE_ROOT + "cart/remove_a_product_from_a_cart_of_a_store.php";
            public static final String GET_DETAIL_OF_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/get_cart_full_details_modified.php";
            public static final String GET_CART_DETAIL_OF_A_USER = ROOT_URL + UserModule.MODULE_ROOT + "cart/get_cart_detail_of_a_user.php";
            public static final String DELETE_A_CART = ROOT_URL + UserModule.MODULE_ROOT + "cart/delete_a_cart.php";
            public static final String GET_CARTS_OF_A_STORE = ROOT_URL + UserModule.MODULE_ROOT + "store/get_carts_of_a_store.php";

            public static final String GET_LAST_DELIVERY_ADDRESS_OF_A_USER = ROOT_URL + UserModule.MODULE_ROOT + "cart/get_last_delivery_address_of_a_user.php";

        }

        public static class Settings{
            public static final String GET_ALL_CITIES = ROOT_URL + UserModule.MODULE_ROOT + "settings/get_all_cities.php";
        }

    }

    // store admin backend apis
    public static class StoreModule {
        private static final String MODULE_ROOT = "shopmodule/";

        public static class Cart {
            public static final String GET_ALL_STORE_CART = ROOT_URL + StoreModule.MODULE_ROOT + "cart/get_filtered_cart_detail.php";
            public static final String CART_REJECT_BY_SHOP = ROOT_URL + StoreModule.MODULE_ROOT + "cart/reject_an_order_by_a_shop.php";
            public static final String CART_CONFIRM_BY_SHOP = ROOT_URL + StoreModule.MODULE_ROOT + "cart/confirm_an_order_by_a_shop.php";
            public static final String CART_ON_DELIVERY_BY_SHOP = ROOT_URL + StoreModule.MODULE_ROOT + "cart/deliver_an_order_by_a_shop.php";
            public static final String CART_PAID_BY_SHOP = ROOT_URL + StoreModule.MODULE_ROOT + "cart/cart_paid_by_a_shop.php";

            public static final String CARTNO = "cartno";
            public static final String STORENO = "storeno";
            public static final String STATUSNO = "statusno";
            public static final String USERNO = "userno";
            public static final String CART_DATETIME = "cartdatetime";
            public static final String DELIVERY_DATETIME = "deliverydatetime";
            public static final String STATUS_DATETIME = "statustime";
            public static final String STATUS_TITLE = "statustitle";
            public static final String DISCOUNT = "discount";
            public static final String COST = "cost";
            public static final String CART_DISCOUNT = "cartdiscount";
            public static final String PAGENO = "pageno";
            public static final String DISTANCE = "distance";
            public static final String SERVICE_CHARGE = "servicerate";
            public static final String DELIVERY_TYPE = "deliverytype";
        }

        public static class Address {
            public static final String STREET = "street";
            public static final String CITY = "city";
            public static final String POSTCODE = "postcode";
            public static final String CONTACT = "contact";
            public static final String LATITUTE = "lat";
            public static final String LONGITUTE = "lon";
        }

        public static class User {
            public static final String USER_FIRST_NAME = "ufirstname";
            public static final String USER_LAST_NAME = "ulastname";
        }

        public static class CartProduct {
            public static final String PRODUCTNO = "productno";
            public static final String UNITNO = "unitno";
            public static final String RATE = "rate";
            public static final String QTY = "qty";
            public static final String DISCOUNT = "discount";
            public static final String PRODUCT_NAME = "productname";
            public static final String PRODUCT_IMAGE = "productimage";
            public static final String UNIT_NAME = "unitname";
            public static final String STOREPRODUCTNO = "store_productno";
            public static final String UNITQTY = "unitqty";
        }

        public static class Bid {
            public static final String BID_ROOT_URL = "http://batikrom.shop/v2/bid/";
            public static final String BID_FEED = BID_ROOT_URL + "bidfeed.php?";
        }

        public static class UserStore {
            public static final String GET_STORES_OF_A_USER = ROOT_URL + StoreModule.MODULE_ROOT + "user/get_all_stores_of_a_user.php";
        }

        public static class StoreProduct {
            public static final String STOREPRODUCTNO = "store_productno";
            public static final String TYPENO = "typeno";
            public static final String STORENO = "storeno";
            public static final String SYSTEM_PRODUCTS = "products";

            public static final String GET_ALL_STORE_PRODUCTS_RELATED_TO_THE_SYSTEM_PRODUCTS = ROOT_URL + StoreModule.MODULE_ROOT + "product/get_all_store_products_related_to_the_system_products.php";

            public static final String GET_ALL_STORE_PAGE_DISPLAY_TYPES = ROOT_URL + StoreModule.MODULE_ROOT + "store/get_store_page_display_types.php";
            public static final String GET_FILTERED_STORE_PRODUCTS = ROOT_URL + StoreModule.MODULE_ROOT + "product/get_filtered_store_products_store.php";
            public static final String ADD_STORE_PRODUCT_IN_DISPLAY_TYPE = ROOT_URL + StoreModule.MODULE_ROOT + "product/insert_store_product_in_display.php";
            public static final String GET_STORE_PAGE_DISPLAY_PRODUCTS = ROOT_URL + StoreModule.MODULE_ROOT + "product/get_store_page_display_products.php";
            public static final String GET_STORE_PRODUCTS_NOT_IN_DISPLAY_TYPES = ROOT_URL + StoreModule.MODULE_ROOT + "product/get_store_products_not_in_display.php";
            public static final String REMOVE_A_PRODUCT_FROM_DISPLAY_TYPE = ROOT_URL + StoreModule.MODULE_ROOT + "product/delete_a_store_product_from_display_type.php";

            //public static final String GET_ALL_STORE_PRODUCTS_RELATED_TO_THE_SYSTEM_PRODUCTS = ROOT_URL + "storeadmin/product/get_all_store_products_related_to_the_system_products.php";
            //public static final String SYSTEM_PRODUCTS = "products";
            //public static final String STORENO = "storeno";
            public static final String TARGET_STORE_NO = "target_storeno";
            public static final String STORE_PRODUCTNO_TO_COPY = "store_productno_to_copy";
            public static final String COPY_A_STORE_PRODUCT_TO_MY_STORE = ROOT_URL + StoreModule.MODULE_ROOT + "product/copy_a_store_product_to_my_store.php";
            public static final String UPDATE_A_STORE_PRODUCT = ROOT_URL + StoreModule.MODULE_ROOT + "product/update_a_store_product.php";
            public static final String INSERT_A_STORE_PRODUCT = ROOT_URL + StoreModule.MODULE_ROOT + "product/insert_a_store_product.php";
        }

        public static class StoreAdminMessage {
            public static final String INSERT_A_STORE_MESSAGE = ROOT_URL + StoreModule.MODULE_ROOT + "store_messages/insert_a_message.php"; // userno, storeno, message
            public static final String GET_A_STORE_MESSAGE = ROOT_URL + StoreModule.MODULE_ROOT + "store_messages/get_messages_of_a_customer.php"; // userno, storeno, pageno
            public static final String GET_A_STORE_MESSAGE_BY_LAST_MSG_ID = ROOT_URL + StoreModule.MODULE_ROOT + "store_messages/get_messages_by_lastslno.php"; // userno, storeno, lastslno
            public static final String CUSTOMER_NO = "customer";
            public static final String STORE_NO = "storeno";
        }

        public static class SystemProduct {
            public static final String STORE_NO = "storeno";
            public static final String GET_FILTER_PRODUCTS = ROOT_URL + StoreModule.MODULE_ROOT + "product/get_filtered_products.php";
            public static final String PAGE_NO = "pageno";
            public static final String PRODUCT_NAME = "productname";
            public static final String GET_SYSTEM_PRODUCT_CATEGORY = ROOT_URL + StoreModule.MODULE_ROOT + "store/get_all_product_categories.php";
            public static final String CATEGORY_NO = "catno";
        }

        public static class StoreDashBoard {
            public static final String GET_STORE_DASHBOARD_DATA = ROOT_URL + StoreModule.MODULE_ROOT + "storedashboard/get_a_store_dashboard_data.php";
        }
    }
}