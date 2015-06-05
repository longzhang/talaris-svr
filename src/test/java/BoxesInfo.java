//import org.springframework.beans.factory.BeanFactoryAware;
//
//import me.ele.talaris.utils.SerializeUtil;
//
//public class BoxesInfo implements BeanFactoryAware {
//    private int result;
//    private DeliveryOrderBox open;
//    private DeliveryOrderBox close;
//
//    public int getResult() {
//        return result;
//    }
//
//    public void setResult(int result) {
//        this.result = result;
//    }
//
//    public DeliveryOrderBox getOpen() {
//        return open;
//    }
//
//    public void setOpen(DeliveryOrderBox open) {
//        this.open = open;
//    }
//
//    public DeliveryOrderBox getClose() {
//        return close;
//    }
//
//    public void setClose(DeliveryOrderBox close) {
//        this.close = close;
//    }
//
//    public BoxesInfo(int result, DeliveryOrderBox open, DeliveryOrderBox close) {
//        super();
//        this.result = result;
//        this.open = open;
//        this.close = close;
//    }
//
//    public BoxesInfo() {
//        super();
//    }
//
//    public static void main(String[] args) {
//        BoxesInfo boxesInfo = new BoxesInfo();
//        DeliveryOrderBox open = new DeliveryOrderBox();
//        DeliveryOrderBox close = new DeliveryOrderBox();
//        boxesInfo.setOpen(open);
//        boxesInfo.setClose(close);
//        System.out.println(SerializeUtil.beanToJson(boxesInfo));
//    }
//
//}
