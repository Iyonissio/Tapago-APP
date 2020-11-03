package com.tapago.app.utils;

import android.util.Log;

import com.tapago.app.model.BasketModel;
import com.tapago.app.model.ProductModel.Datum;

import java.util.ArrayList;

public class BasketUtils {

    public static BasketModel getBasketObj(Datum datum) {
        BasketModel basketItem = new BasketModel();
        try {
            if (!datum.isMostpopular()) {
                basketItem.setProductId(datum.getId());
                basketItem.setProductName(datum.getProductName());
                basketItem.setAboutProduct(datum.getAboutProduct());
                basketItem.setMainImage(datum.getMainImage());
                basketItem.setProductType(datum.getProductType());
                basketItem.setProductBrand(datum.getProductBrand());
                basketItem.setImageUrl(datum.getImageUrl());
                basketItem.setDefaultImage(datum.getDefaultImage());
                if (datum.getProductSize() != null) {
                    basketItem.setProductSizeList(datum.getProductSize());
                }
                if (datum.getProductImage() != null) {
                    basketItem.setProductImageList(datum.getProductImage());
                }
                basketItem.setRepeatItem(datum.isRepeatItem());
                basketItem.setMinus(datum.isMinus());
                basketItem.setAddedQuantity(datum.getAddedQuantity());
                basketItem.setMainQuantity(datum.getMainQuantity());
                basketItem.setPrice(datum.getPrice());
                basketItem.setProductScale(datum.getProductScale());
                basketItem.setSameScale(datum.isSameScale());
                basketItem.setCategoryId(Integer.parseInt(datum.getCategoryId()));
            } else {
                basketItem.setProductId(datum.getId());
                basketItem.setProductName(datum.getProductName());
                basketItem.setAboutProduct(datum.getAboutProduct());
                basketItem.setMainImage(datum.getMainImage());
                basketItem.setProductType(datum.getProductType());
                basketItem.setProductBrand(datum.getProductBrand());
                basketItem.setImageUrl(datum.getImageUrl());
                basketItem.setDefaultImage(datum.getDefaultImage());
                if (datum.getProductSize() != null) {
                    basketItem.setProductSizeList(datum.getProductSize());
                }
                if (datum.getProductImage() != null) {
                    basketItem.setProductImageList(datum.getProductImage());
                }
                basketItem.setRepeatItem(datum.isRepeatItem());
                basketItem.setMinus(datum.isMinus());
                basketItem.setAddedQuantity(datum.getAddedQuantity());
                basketItem.setMainQuantity(datum.getMainQuantity());
                basketItem.setPrice(datum.getPrice());
                basketItem.setProductScale(datum.getProductScale());
                basketItem.setSameScale(datum.isSameScale());
                basketItem.setCategoryId(Integer.parseInt(datum.getCategoryIds()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return basketItem;
    }


    public static void addInBasket(BasketModel basketItem) {
        ArrayList<BasketModel> basketListing;
        ArrayList<BasketModel> guestBasketListing = MySharedPreferences.getGuestBasketListing();
        if (guestBasketListing != null) {
            basketListing = new ArrayList<>(guestBasketListing);
        } else {
            basketListing = new ArrayList<>();
        }

        if (basketListing.isEmpty()) {
            basketListing = new ArrayList<>();
            basketListing.add(basketItem);
        } else {
            ArrayList<BasketModel> tempList = new ArrayList<>(basketListing);
            ArrayList<BasketModel> tempList1 = new ArrayList<>();
            boolean isAlreadyAdded = false;
            if (basketItem.getAddedQuantity() == 0) {
                for (BasketModel bItem : tempList) {
                    if (bItem.getProductId().equals(basketItem.getProductId())) {
                        basketListing.remove(bItem);
                        break;
                    }
                }
            } else {
                if (basketItem.isRepeatItem()) {
                    for (int i = 0; i < tempList.size(); i++) {
                        if (tempList.get(i).getProductId().equals(basketItem.getProductId())) {
                            tempList1.add(getObj(tempList.get(i)));
                        }
                    }
                    int count = 0;
                    for (int i = 0; i < tempList1.size(); i++) {
                        count++;
                        if (count == tempList1.size()) {
                            for (int j = 0; j < tempList.size(); j++) {
                                if (tempList.get(j).getProductId().equals(tempList1.get(i).getProductId())) {
                                    tempList.get(j).setAddedQuantity(basketItem.getAddedQuantity());
                                    tempList.get(j).setMainQuantity(basketItem.getMainQuantity());
                                    tempList.get(j).setPrice(basketItem.getPrice());
                                    tempList.get(j).setProductScale(basketItem.getProductScale());
                                    isAlreadyAdded = true;
                                }
                            }
                        }
                    }
                } else if (basketItem.isMinus()) {
                    for (int i = 0; i < tempList.size(); i++) {
                        if (tempList.get(i).getProductId().equals(basketItem.getProductId())) {
                            tempList1.add(getObj(tempList.get(i)));
                        }
                    }
                    int count = 0;
                    for (int i = 0; i < tempList1.size(); i++) {
                        count++;
                        if (count == tempList1.size()) {
                            int counts = 0;
                            for (int j = 0; j < tempList.size(); j++) {
                                if (tempList.get(j).getProductId().equals(tempList1.get(i).getProductId())) {
                                    counts++;
                                    if (counts == tempList1.size()) {
                                        tempList.get(j).setAddedQuantity(tempList.get(j).getAddedQuantity() - 1);
                                        tempList.get(j).setMainQuantity(tempList.get(j).getMainQuantity() - 1);
                                        if (tempList.get(j).getAddedQuantity() == 0) {
                                            basketListing.remove(j);
                                        }
                                        isAlreadyAdded = true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < tempList.size(); i++) {
                        if (tempList.get(i).getProductId().equals(basketItem.getProductId()) && tempList.get(i).getProductScale().equals(basketItem.getProductScale())) {
                            tempList.get(i).setAddedQuantity(basketItem.getAddedQuantity());
                            tempList.get(i).setMainQuantity(basketItem.getMainQuantity());
                            tempList.get(i).setPrice(basketItem.getPrice());
                            tempList.get(i).setProductScale(basketItem.getProductScale());
                            isAlreadyAdded = true;
                        }

                    }
                }
                if (!isAlreadyAdded) {
                    basketListing.add(basketItem);
                }
            }
        }
        Log.e("basketSize", String.valueOf(basketListing.size()));
        MySharedPreferences.getMySharedPreferences().setGuestBasketListing(basketListing);
    }

    private static BasketModel getObj(BasketModel datum) {
        BasketModel basketItem = new BasketModel();

        try {
            basketItem.setProductId(datum.getProductId());
            basketItem.setProductName(datum.getProductName());
            basketItem.setAboutProduct(datum.getAboutProduct());
            basketItem.setMainImage(datum.getMainImage());
            basketItem.setProductType(datum.getProductType());
            basketItem.setImageUrl(datum.getImageUrl());
            basketItem.setDefaultImage(datum.getDefaultImage());
            if (datum.getProductSizeList() != null) {
                basketItem.setProductSizeList(datum.getProductSizeList());
            }
            if (datum.getProductImageList() != null) {
                basketItem.setProductImageList(datum.getProductImageList());
            }
            basketItem.setRepeatItem(datum.isRepeatItem());
            basketItem.setMinus(datum.isMinus());
            basketItem.setAddedQuantity(datum.getAddedQuantity());
            basketItem.setMainQuantity(datum.getMainQuantity());
            basketItem.setPrice(datum.getPrice());
            basketItem.setProductScale(datum.getProductScale());
            basketItem.setProductBrand(datum.getProductBrand());
            basketItem.setSameScale(datum.isSameScale());
            basketItem.setCategoryId(datum.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basketItem;
    }

    public static BasketModel getCartObj(BasketModel datum) {
        BasketModel basketItem = new BasketModel();

        try {
            basketItem.setProductId(datum.getProductId());
            basketItem.setProductName(datum.getProductName());
            basketItem.setAboutProduct(datum.getAboutProduct());
            basketItem.setMainImage(datum.getMainImage());
            basketItem.setProductType(datum.getProductType());
            basketItem.setProductBrand(datum.getProductBrand());
            basketItem.setImageUrl(datum.getImageUrl());
            basketItem.setDefaultImage(datum.getDefaultImage());
            if (datum.getProductSizeList() != null) {
                basketItem.setProductSizeList(datum.getProductSizeList());
            }
            if (datum.getProductImageList() != null) {
                basketItem.setProductImageList(datum.getProductImageList());
            }
            basketItem.setRepeatItem(datum.isRepeatItem());
            basketItem.setMinus(datum.isMinus());
            basketItem.setAddedQuantity(datum.getAddedQuantity());
            basketItem.setMainQuantity(datum.getMainQuantity());
            basketItem.setPrice(datum.getPrice());
            basketItem.setProductScale(datum.getProductScale());
            basketItem.setSameScale(datum.isSameScale());
            basketItem.setCategoryId(datum.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basketItem;
    }


    public static void addCart(BasketModel basketItem) {
        ArrayList<BasketModel> basketListing;
        ArrayList<BasketModel> guestBasketListing = MySharedPreferences.getGuestBasketListing();
        if (guestBasketListing != null) {
            basketListing = new ArrayList<>(guestBasketListing);
        } else {
            basketListing = new ArrayList<>();
        }

        if (basketListing.isEmpty()) {
            basketListing = new ArrayList<>();
            basketListing.add(basketItem);
        } else {
            ArrayList<BasketModel> tempList = new ArrayList<>(basketListing);
            if (basketItem.getAddedQuantity() == 0) {
                for (BasketModel bItem : tempList) {
                    if (bItem.getProductId().equals(basketItem.getProductId())) {
                        basketListing.remove(bItem);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getProductId().equals(basketItem.getProductId()) && tempList.get(i).getProductScale().equals(basketItem.getProductScale())) {
                       /* if (basketItem.isBundleItem()) {
                            if (basketItem.getBundleItemList() != null) {
                                if (tempList.get(i).getBundleItemList().toString().equals(basketItem.getBundleItemList().toString())) {
                                    tempList.get(i).setAddedQuantity(basketItem.getAddedQuantity());
                                    tempList.get(i).setMainQuantity(basketItem.getMainQuantity());
                                }
                            }
                        } else {
                            if (basketItem.getRemovableIngredient() != null || basketItem.getMainIngredient() != null) {
                                if (basketItem.getRemovableIngredient().size() > 0 || basketItem.getMainIngredient().size() > 0) {
                                    if (basketItem.getRemovableIngredient().toString().equals(tempList.get(i).getRemovableIngredient().toString()) && basketItem.getMainIngredient().toString().equals(tempList.get(i).getMainIngredient().toString())) {
                                        tempList.get(i).setAddedQuantity(basketItem.getAddedQuantity());
                                        tempList.get(i).setMainQuantity(basketItem.getMainQuantity());
                                    }
                                } else {
                                    tempList.get(i).setAddedQuantity(basketItem.getAddedQuantity());
                                    tempList.get(i).setMainQuantity(basketItem.getMainQuantity());
                                }
                            } else {*/
                        tempList.get(i).setAddedQuantity(basketItem.getAddedQuantity());
                        tempList.get(i).setMainQuantity(basketItem.getMainQuantity());
                        // }
                        // }
                        // break;
                    }
                }
            }
        }
        MySharedPreferences.getMySharedPreferences().setGuestBasketListing(basketListing);
    }
}
