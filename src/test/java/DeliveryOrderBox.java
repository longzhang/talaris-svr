public class DeliveryOrderBox {
    private int id;
    private String tag;
    private String sn;
    private int status;
    private int dispatch_id;
    private int terminal_area_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDispatch_id() {
        return dispatch_id;
    }

    public void setDispatch_id(int dispatch_id) {
        this.dispatch_id = dispatch_id;
    }

    public int getTerminal_area_id() {
        return terminal_area_id;
    }

    public void setTerminal_area_id(int terminal_area_id) {
        this.terminal_area_id = terminal_area_id;
    }

    public DeliveryOrderBox() {
        super();
    }

    public DeliveryOrderBox(int id, String tag, String sn, int status, int dispatch_id, int terminal_area_id) {
        super();
        this.id = id;
        this.tag = tag;
        this.sn = sn;
        this.status = status;
        this.dispatch_id = dispatch_id;
        this.terminal_area_id = terminal_area_id;
    }

}
