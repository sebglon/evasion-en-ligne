/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.service.model;

/**
 *
 * @author sgl
 */
public enum ContentConst {
    STATIC("STATIC"), TEMPLATE("TEMPLATE"), TEMPLATE_URL("TEMPLATE_URL");
    private final String code;

    public String getCode() {
        return code;
    }

    private ContentConst(String code) {
        this.code = code;
    }
    
    public static ContentConst valueOfCode(String code) {
        for (ContentConst type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
            "Partnership status cannot be resolved for code " + code);
    }

    @Override
    public String toString() {
        return  code;
    }
    

}
