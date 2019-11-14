import javax.swing.*;
public class Summary{
    JFrame summary = new JFrame("Summary");
    JLabel name = new JLabel();
    JLabel VehicleNum = new JLabel();
    JLabel phoneNum = new JLabel();
    JLabel rating = new JLabel();
    JLabel fare = new JLabel();
    JLabel duration = new JLabel();
    Summary(String DriverName,String vehiclenumString,String PhoneNum,Double Fare,int hours,int minutes,double Rating,Double travelDist){//showing assigned details to user
        name.setText("Driver Name: "+DriverName);
        VehicleNum.setText("Vehicle ID: "+vehiclenumString);
        phoneNum.setText("Phone No: "+PhoneNum);
        rating.setText("Rating: "+String.valueOf(Rating));
        fare.setText("Estimated Fare: Rs."+String.valueOf(Fare.intValue()));
        duration.setText("Estimated Duration: "+String.valueOf(hours)+"hours "+String.valueOf(minutes)+"minutes");
        name.setBounds(0, 0, 300, 50);
        VehicleNum.setBounds(0, 50, 300, 50);
        phoneNum.setBounds(0, 100, 300, 50);
        rating.setBounds(0, 150, 300, 50);
        fare.setBounds(0, 200, 300, 50);
        duration.setBounds(0, 250, 300, 50);
        summary.add(name);
        summary.add(VehicleNum);
        summary.add(phoneNum);
        summary.add(rating);
        summary.add(fare);
        summary.add(duration);
        summary.setLayout(null);
        summary.setExtendedState(JFrame.MAXIMIZED_BOTH);
        summary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
