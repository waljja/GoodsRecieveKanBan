package ht.model.model108;

public class StockLocationsWithBLOBs extends StockLocations {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column StockLocations.Name
     *
     * @mbggenerated Mon Nov 21 13:51:24 CST 2022
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column StockLocations.Identifier
     *
     * @mbggenerated Mon Nov 21 13:51:24 CST 2022
     */
    private String identifier;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column StockLocations.Name
     *
     * @return the value of StockLocations.Name
     *
     * @mbggenerated Mon Nov 21 13:51:24 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column StockLocations.Name
     *
     * @param name the value for StockLocations.Name
     *
     * @mbggenerated Mon Nov 21 13:51:24 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column StockLocations.Identifier
     *
     * @return the value of StockLocations.Identifier
     *
     * @mbggenerated Mon Nov 21 13:51:24 CST 2022
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column StockLocations.Identifier
     *
     * @param identifier the value for StockLocations.Identifier
     *
     * @mbggenerated Mon Nov 21 13:51:24 CST 2022
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier == null ? null : identifier.trim();
    }
}