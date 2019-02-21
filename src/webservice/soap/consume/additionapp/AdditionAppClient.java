package webservice.soap.consume.additionapp;

public class AdditionAppClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CalculatorMsSOAPHTTPService calcSvc = new CalculatorMsSOAPHTTPService();
		CalculatorMsPortType calcIface = calcSvc.getCalculatorMsSOAPHTTPPort();
		
		ReqAdd reqAddMsg = new ReqAdd();
		reqAddMsg.intA= 6;
		reqAddMsg.intB= 7;
		ResAdd resAddMsg = new ResAdd();		
		try {
			resAddMsg = calcIface.addition(reqAddMsg);
		} catch (AdditionFault1 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Result: " + resAddMsg.addC);
	}

}
