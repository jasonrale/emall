package com.jason.emall.entity;

/**
 * Database Table Remarks:
 *   此表为收货地址表。
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table shipping
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class Shipping {
    /**
     * Database Column Remarks:
     *   收货地址id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.s_id
     *
     * @mbg.generated
     */
    private Integer sId;

    /**
     * Database Column Remarks:
     *   收货人名字
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.s_name
     *
     * @mbg.generated
     */
    private String sName;

    /**
     * Database Column Remarks:
     *   收货人手机号码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.s_mobile_number
     *
     * @mbg.generated
     */
    private Integer sMobileNumber;

    /**
     * Database Column Remarks:
     *   详细地址
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.s_address
     *
     * @mbg.generated
     */
    private String sAddress;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.s_id
     *
     * @return the value of shipping.s_id
     *
     * @mbg.generated
     */
    public Integer getsId() {
        return sId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.s_id
     *
     * @param sId the value for shipping.s_id
     *
     * @mbg.generated
     */
    public void setsId(Integer sId) {
        this.sId = sId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.s_name
     *
     * @return the value of shipping.s_name
     *
     * @mbg.generated
     */
    public String getsName() {
        return sName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.s_name
     *
     * @param sName the value for shipping.s_name
     *
     * @mbg.generated
     */
    public void setsName(String sName) {
        this.sName = sName == null ? null : sName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.s_mobile_number
     *
     * @return the value of shipping.s_mobile_number
     *
     * @mbg.generated
     */
    public Integer getsMobileNumber() {
        return sMobileNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.s_mobile_number
     *
     * @param sMobileNumber the value for shipping.s_mobile_number
     *
     * @mbg.generated
     */
    public void setsMobileNumber(Integer sMobileNumber) {
        this.sMobileNumber = sMobileNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.s_address
     *
     * @return the value of shipping.s_address
     *
     * @mbg.generated
     */
    public String getsAddress() {
        return sAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.s_address
     *
     * @param sAddress the value for shipping.s_address
     *
     * @mbg.generated
     */
    public void setsAddress(String sAddress) {
        this.sAddress = sAddress == null ? null : sAddress.trim();
    }
}