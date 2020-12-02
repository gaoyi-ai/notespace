package algorithm;

import java.util.List;

public interface Parity {
    int evenOddParity();
    List<Integer> HMParity();
    List<Integer> CRCParity(List<Integer> polynomial);
}
