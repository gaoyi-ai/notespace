package algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Transmitting {
    public static final int bitNumber = 6;
    public static final List<Integer> polynomial = Arrays.asList(1, 1, 0, 1);
    static Transmitter transmitter;
    static Receiver receiver;

    public static void eop() {
        // generate random data
        Data data = new Data(bitNumber, ParityType.Even);
        data.setData(Utils.getRandom());
        // compute parity
        transmitter = new Transmitter();
        transmitter.setData(data);
        data.setParity(Collections.singletonList(transmitter.evenOddParity()));
        System.out.println(data);
        System.out.println("--------------------------");
        // simulate transmit
        Utils.transmit(data);
        // receive
        receiver = new Receiver(data);
        Data receiverData = receiver.getData();
        // verify data
        System.out.println(data);
        System.out.println("--------------------------");
        List<Integer> dataParity = receiverData.getParity();
        int computeParity = receiver.evenOddParity();
        System.out.println("dataParity: " + dataParity + "\tcomputeParity: " + computeParity);
    }

    public static void crc() {
        Data data = new Data(bitNumber, ParityType.CRC);
        data.setData(Arrays.asList(0, 0, 1, 1, 0, 1));
        transmitter = new Transmitter();
        transmitter.setData(data);
        data.setParity(transmitter.CRCParity(polynomial));
        System.out.println(data);
        System.out.println("--------------------------");
        // simulate transmit
        Utils.transmit(data);
        // receive
        receiver = new Receiver(data);
        Data receiverData = receiver.getData();
        // verify data
        System.out.println(data);
        System.out.println("--------------------------");
        List<Integer> dataParity = receiverData.getParity();
        List<Integer> computeParity = receiver.CRCParity(polynomial);
        System.out.println("dataParity: " + dataParity + "\tcomputeParity: " + computeParity);
    }

    public static void hm() {
        HMData data = new HMData(4, ParityType.HM);
        data.setData(Arrays.asList(1, 1, 0, 1));
        transmitter = new Transmitter();
        transmitter.setData(data);
        data.setDataWithParity(transmitter.HMParity());
        System.out.println(data);
        System.out.println("--------------------------");
        // simulate transmit
        Utils.transmit(data);
        // receive
        receiver = new Receiver(data);
        HMData receiverData = (HMData) receiver.getData();
        // verify data
        System.out.println(data);
        System.out.println("--------------------------");
        List<Integer> dataWithParity = receiverData.getDataWithParity();
        List<Integer> computeParity = receiver.HMParity();
        System.out.println("dataWithParity: " + dataWithParity + "\tcomputeParity: " + computeParity);
        System.out.println("if Only 1 bit error, the bit position need to fix: " + (receiver.HMParityDirectFixError() + 1));
    }

    public static void main(String[] args) {
//        eop();
//        crc();
        hm();
    }
}
