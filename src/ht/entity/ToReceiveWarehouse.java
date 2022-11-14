package ht.entity;

/**
 * 待入仓物料看板model
 * @author 刘惠明
 * @date 2020-9-3
 */
public class ToReceiveWarehouse {
    private int id;
    private String GRN;
    /** 物料编号 */
    private String ItemNumber;
    /** GRN数量 */
    private String GRNQuantity;
    /** 收货库位 */
    private String ReceivingLocation;
    /** 生产需要时间 */
    private String ProductionTime;
    /** Aegis-合格 */
    private String AegisQualify;
    /** SAP-合格 */
    private String SAPQualify;
    /** 待入主料仓时间 */
    private String WaitTimeToMainbin;
	private String ReturnWarehouseTime;
    private String plant;
    private String UID;
    private String type;
    private int sequence;
	private String createdate;
	private String closeDate;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getGRN() {
        return GRN;
    }
    public void setGRN(String grn) {
        GRN = grn;
    }
    public String getItemNumber() {
        return ItemNumber;
    }
    public void setItemNumber(String itemNumber) {
        ItemNumber = itemNumber;
    }
    public String getGRNQuantity() {
        return GRNQuantity;
    }
    public void setGRNQuantity(String quantity) {
        GRNQuantity = quantity;
    }
    public String getReceivingLocation() {
        return ReceivingLocation;
    }
    public void setReceivingLocation(String receivingLocation) {
        ReceivingLocation = receivingLocation;
    }
    public String getProductionTime() {
        return ProductionTime;
    }
    public void setProductionTime(String productionTime) {
        ProductionTime = productionTime;
    }
    public String getAegisQualify() {
        return AegisQualify;
    }
    public void setAegisQualify(String aegisQualify) {
        AegisQualify = aegisQualify;
    }
    public String getSAPQualify() {
        return SAPQualify;
    }
    public void setSAPQualify(String qualify) {
        SAPQualify = qualify;
    }
    public String getWaitTimeToMainbin() {
        return WaitTimeToMainbin;
    }
    public void setWaitTimeToMainbin(String waitTimeToMainbin) {
        WaitTimeToMainbin = waitTimeToMainbin;
    }
    public String getPlant() {
        return plant;
    }
    public void setPlant(String plant) {
        this.plant = plant;
    }
    public String getUID() {
        return UID;
    }
    public void setUID(String uid) {
        UID = uid;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	public String getReturnWarehouseTime() {
		return ReturnWarehouseTime;
	}
	public void setReturnWarehouseTime(String returnWarehouseTime) {
		ReturnWarehouseTime = returnWarehouseTime;
	}
}
