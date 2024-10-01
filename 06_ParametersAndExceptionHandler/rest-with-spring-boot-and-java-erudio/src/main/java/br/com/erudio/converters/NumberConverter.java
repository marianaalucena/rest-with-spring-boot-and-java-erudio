package br.com.erudio.converters;

public class NumberConverter {
	
	public static boolean isZero(String strNumber) {
		if(strNumber == null) return true;
		String number = strNumber.replaceAll(",", ".");
		if(number == "0" || number == "0.0") {
			return true;
		}
		return false;
	}
	
	public static Double convertToDouble(String strNumber) {
		if(strNumber == null) return 0D;
		String number = strNumber.replaceAll(",", ".");
		if(isNumeric(number)) return Double.parseDouble(number);
		return 0D;
	}

	public static boolean isNumeric(String strNumber) {
		if(strNumber == null) return false;
		String number = strNumber.replaceAll(",", ".");
		return number.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
}
