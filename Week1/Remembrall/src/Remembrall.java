public class Remembrall {
    public static boolean isPhoneNumberForgettable(String phoneNumber){
        if(phoneNumber == null) return false;
        if(phoneNumber.equals("")) return false;

        for(int i = 0; i < phoneNumber.length(); i++){
                if (phoneNumber.charAt(i) != ')' && phoneNumber.charAt(i) != '(' && phoneNumber.charAt(i) != ' ' &&
                    phoneNumber.charAt(i) != '-' &&!Character.isDigit(phoneNumber.charAt(i))) return true;
        }
        for(int i = 0; i < phoneNumber.length() - 1; i++) {
            if (Character.isDigit(phoneNumber.charAt(i))) {
                for (int j = i + 1; j < phoneNumber.length(); j++) {
                    if (phoneNumber.charAt(i) == phoneNumber.charAt(j)) return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(Remembrall.isPhoneNumberForgettable(""));
        // More Tests
        // System.out.println(Remembrall.isPhoneNumberForgettable(null));
        // System.out.println(Remembrall.isPhoneNumberForgettable("498-123-123"));
        // System.out.println(Remembrall.isPhoneNumberForgettable("0894 123 567"));
        // System.out.println(Remembrall.isPhoneNumberForgettable("(888)-FLOWERS"));
        // System.out.println(Remembrall.isPhoneNumberForgettable("(444)-greens"));
    }
}
