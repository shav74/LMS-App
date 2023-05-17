package com.example.warehousebot;

public class LecMaterials {

    String materialName, materialDetails, materialLink, materialExtraDetails, type;

    public LecMaterials(){};
    public LecMaterials(String materialName, String materialDetails, String materialLink, String materialExtraDetails, String type) {
        this.materialName = materialName;
        this.materialDetails = materialDetails;
        this.materialLink = materialLink;
        this.materialExtraDetails = materialExtraDetails;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialDetails() {
        return materialDetails;
    }

    public void setMaterialDetails(String materialDetails) {
        this.materialDetails = materialDetails;
    }

    public String getMaterialLink() {
        return materialLink;
    }

    public void setMaterialLink(String materialLink) {
        this.materialLink = materialLink;
    }

    public String getMaterialExtraDetails() {
        return materialExtraDetails;
    }

    public void setMaterialExtraDetails(String materialExtraDetails) {
        this.materialExtraDetails = materialExtraDetails;
    }
}
