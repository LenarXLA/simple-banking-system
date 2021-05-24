package banking;

import java.util.Random;

public class Utils {

    public String generateCard() {
        Random random = new Random();
        String num = "400000" + (random.nextInt(899999999) + 100000000);
        return num + checkNumberLuhn(num);
    }


    private String checkNumberLuhn(String cardNumber) {
        cardNumber += "0";
        int odd = 0;
        int even = 0;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int currentNum = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 != 0) {
                odd += currentNum;
            } else {
                if (currentNum * 2 > 9) {
                    even += currentNum * 2 - 9;
                } else {
                    even += currentNum * 2;
                }
            }
        }
        int controlSum = odd + even;
        return String.valueOf(controlSum % 10 == 0 ? 0 : 10 - controlSum % 10);
    }

    public String generatePin() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9000) + 1000);
    }

    public boolean checkCardForLuhn(String cardNum) {
        boolean res = false;
        String currentNum = cardNum.substring(0, cardNum.length() - 1);
        String endOfNum = cardNum.substring(cardNum.length() - 1);

        if (checkNumberLuhn(currentNum).equals(endOfNum)) {
            res = true;
        }
        return res;
    }
}
