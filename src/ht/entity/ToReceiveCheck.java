package ht.entity;

import java.util.Date;


/**
 * 待点收看板实体类
 * @author 刘惠明
 * @createDate 2020-9-3
 * @updateUser 丁国钊
 * @updateDate 2022-11-3
 * @updateRemark 修改注释
 */
public class ToReceiveCheck {
	private int id;
	private String GRN;
	/** 物料编号 */
	private String ItemNumber;
	/** GRN数量 */
	private String GRNQuantity;
	/** UID数量 */
	private String UIDQuantity;
	/** 生产需要时间，与急料类别有关 */
	private String ProductionTime;
	/** 待收料等待时间 */
	private String waittime;
	/** 工厂 */
	private String plant;
	private String UID;
	private String GRNDATE;
	private String GRN103;
	/** 急料类别 */
	private String type;
	private int sequence;
	/** 关闭时间 */
	private Date closeDate;
	private String createdate;
	
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
    public String getUIDQuantity() {
        return UIDQuantity;
    }
    public void setUIDQuantity(String quantity) {
        UIDQuantity = quantity;
    }
    public String getProductionTime() {
        return ProductionTime;
    }
    public void setProductionTime(String productionTime) {
        ProductionTime = productionTime;
    }
	public String getWaittime() {
        return waittime;
    }
    public void setWaittime(String waittime) {
        this.waittime = waittime;
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
	public String getGRNDATE() {
		return GRNDATE;
	}
	public void setGRNDATE(String grndate) {
		GRNDATE = grndate;
	}
	public String getGRN103() {
		return GRN103;
	}
	public void setGRN103(String grn103) {
		GRN103 = grn103;
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
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
}