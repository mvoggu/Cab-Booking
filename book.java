import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class book{
    JFrame bookingPage = new JFrame("Booking Page");
    JLabel pickupPointText = new JLabel("Select Pickup Point: "); 
    JLabel dropPointText = new JLabel("Select Drop Point: "); 
    JLabel walletText = new JLabel("Wallet: ");
    JTextField walletMoney = new JTextField();
    JComboBox<String> pickupPoint = new JComboBox<String>();
    JComboBox<String> dropPoint = new JComboBox<String>();
    JButton bookButton = new JButton("BOOK");
    JButton addMoney = new JButton("ADD MONEY");
    JButton viewBalance = new JButton("VIEW BALANCE");
    book(String userID){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "mysecretpass");
            Statement stmt = conn.createStatement();
            stmt.execute("USE CABBOOKING");
            ResultSet rs = stmt.executeQuery("SELECT NAME FROM LOCATIONS");
            while(rs.next()){
                pickupPoint.addItem(rs.getString("NAME"));
                dropPoint.addItem(rs.getString("NAME"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        pickupPointText.setBounds(0, 0, 150, 25);
        dropPointText.setBounds(0, 25, 150, 25);
        walletText.setBounds(0, 120, 70, 25);
        pickupPoint.setBounds(150, 0, 150, 25);
        dropPoint.setBounds(150, 25, 150, 25);
        walletMoney.setBounds(70, 120, 100, 25);
        bookButton.setBounds(170, 50, 80, 30);
        addMoney.setBounds(170, 120, 150, 30);
        viewBalance.setBounds(320, 120, 150, 30);

        viewBalance.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "mysecretpass");
                    Statement stmt = conn.createStatement();
                    stmt.execute("USE CABBOOKING");
                    ResultSet balance = stmt.executeQuery("SELECT MONEY FROM USERS WHERE USERID="+userID);
                    if(balance.next()){
                        ToastMessage toastMessage = new ToastMessage("WALLET HAS Rs."+balance.getString("MONEY"),3000);//showing balance in the wallet
                        toastMessage.setVisible(true);
                    }

                } catch (Exception walleException) {
                    System.out.println(walleException);
                }
            }
        });


        addMoney.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "mysecretpass");
                    Statement stmt = conn.createStatement();
                    stmt.execute("USE CABBOOKING");
                    stmt.executeUpdate("UPDATE USERS SET MONEY=MONEY+"+walletMoney.getText()+" WHERE USERID="+userID);//adding money to wallet
                    ToastMessage toastMessage = new ToastMessage("MONEY ADDED",3000);
                    toastMessage.setVisible(true);

                } catch (Exception walleException) {
                    System.out.println(walleException);
                }
            }
        });

        bookButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "mysecretpass");
                    Statement stmt = conn.createStatement();
                    stmt.execute("USE CABBOOKING");
                    ResultSet walletMoney = stmt.executeQuery("SELECT MONEY FROM USERS WHERE USERID="+userID);
                    double moneyInWallet = 0.0;
                    if(walletMoney.next()){
                        moneyInWallet = Double.parseDouble(walletMoney.getString("MONEY"));
                    }
                    ResultSet userFree = stmt.executeQuery("SELECT * FROM USERS WHERE ((BusyTill IS NULL) OR (now() > BusyTill)) AND USERID="+userID);
                    if(moneyInWallet<300.0 || !userFree.next()){//checking min money in the wallet and also checking whether user already in a ride 
                        if(moneyInWallet<300.0){
                            ToastMessage toastMessage = new ToastMessage("INSUFFICIENT WALLET MONEY,MINIMUM IS Rs.300",3000);
                            toastMessage.setVisible(true);
                        }
                        if(!userFree.next()){
                            ToastMessage toastMessage = new ToastMessage("USER ALREADY IN RIDE",3000);
                            toastMessage.setVisible(true);
                        }
                    }
                    else{
                        ResultSet totalTripSet = stmt.executeQuery("SELECT SUM(TRIPS) FROM DRIVERS");
                        int trips = 0;
                        if(totalTripSet.next()){
                            trips = Integer.parseInt(totalTripSet.getString("SUM(TRIPS)"));
                        }
                        if(trips==5){
                            ResultSet noAvailableDriverSet = stmt.executeQuery("SELECT NAME FROM DRIVERS WHERE (BusyTill IS NULL) OR (now() > BusyTill)");
                            int noOfAvailableDrivers = 0;
                            String driverNames[] = new String[4];
                            while(noAvailableDriverSet.next()){
                                driverNames[noOfAvailableDrivers] = noAvailableDriverSet.getString("NAME");//reallocation
                                noOfAvailableDrivers++;
                            }
                            switch(noOfAvailableDrivers){
                                case 2:
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=10.0,LONGITUDE=20.0 WHERE NAME=\""+driverNames[0]+"\"");
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=13.0,LONGITUDE=23.0 WHERE NAME=\""+driverNames[1]+"\"");
                                    break;
                                case 3:
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=10.0,LONGITUDE=20.0 WHERE NAME=\""+driverNames[0]+"\"");
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=11.0,LONGITUDE=21.0 WHERE NAME=\""+driverNames[1]+"\"");
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=13.0,LONGITUDE=23.0 WHERE NAME=\""+driverNames[2]+"\"");
                                    break;
                                case 4:
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=10.0,LONGITUDE=20.0 WHERE NAME=\""+driverNames[0]+"\"");
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=11.0,LONGITUDE=21.0 WHERE NAME=\""+driverNames[1]+"\"");
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=12.0,LONGITUDE=22.0 WHERE NAME=\""+driverNames[2]+"\"");
                                    stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE=13.0,LONGITUDE=23.0 WHERE NAME=\""+driverNames[3]+"\"");
                                    break;
                            }
                        }
                        Double lat1=0.0,lat2=0.0,long1=0.0,long2=0.0;
                        ResultSet pickUpData = stmt.executeQuery("SELECT * FROM LOCATIONS WHERE NAME=\""+pickupPoint.getSelectedItem()+"\"");
                        if(pickUpData.next()){
                            lat1 = Double.parseDouble(pickUpData.getString("LATITUDE"));
                            long1 = Double.parseDouble(pickUpData.getString("LONGITUDE"));   
                        }
                        ResultSet dropData = stmt.executeQuery("SELECT * FROM LOCATIONS WHERE NAME=\""+dropPoint.getSelectedItem()+"\"");
                        if(dropData.next()){
                            lat2 = Double.parseDouble(dropData.getString("LATITUDE"));
                            long2 = Double.parseDouble(dropData.getString("LONGITUDE"));   
                        }
                        Double travelDist =  distance(lat1, lat2, long1, long2, 0.0, 0.0); //in metres
                        Double Fare = (travelDist/1000.0)*0.5; //in rupees , assuming 0.5rupees per KM
                        Double Duration = (travelDist/1000.0)/1000; //Duration in hours, assuming 1000Kmph as speed
                        String driverName="",VehicleNum = "",phoneNum = "";
    
                        ResultSet AvailableDrivers = stmt.executeQuery("SELECT * FROM DRIVERS WHERE (BusyTill IS NULL) OR (now() > BusyTill)");//checking for busyfree drivers who are a min distance
                        Double minDistance = 0.0;
                        boolean DriversAvailable = true;
                        if(AvailableDrivers.next()){
                            minDistance = distance(lat1,Double.parseDouble(AvailableDrivers.getString("LATITUDE")), long1,Double.parseDouble(AvailableDrivers.getString("LONGITUDE")), 0.0, 0.0);
                        }
                        else{
                            ToastMessage toastMessage = new ToastMessage("TIMEOUT REACHED,ALL DRIVERS BUSY",3000);
                            toastMessage.setVisible(true);
                            DriversAvailable = false;
                        }
                        while(AvailableDrivers.next()){
                            if(distance(lat1,Double.parseDouble(AvailableDrivers.getString("LATITUDE")), long1,Double.parseDouble(AvailableDrivers.getString("LONGITUDE")), 0.0, 0.0)<minDistance){
                                minDistance=distance(lat1,Double.parseDouble(AvailableDrivers.getString("LATITUDE")), long1,Double.parseDouble(AvailableDrivers.getString("LONGITUDE")), 0.0, 0.0);
                            }
                        }
                        Double maxRating = 0.0;
                        AvailableDrivers = stmt.executeQuery("SELECT * FROM DRIVERS WHERE (BusyTill IS NULL) OR (now() > BusyTill)");//drivers with max rating
                        while(AvailableDrivers.next()){
                            if(distance(lat1,Double.parseDouble(AvailableDrivers.getString("LATITUDE")), long1,Double.parseDouble(AvailableDrivers.getString("LONGITUDE")), 0.0, 0.0)==minDistance){
                                if(Double.parseDouble(AvailableDrivers.getString("RATING")) > maxRating){
                                    maxRating = Double.parseDouble(AvailableDrivers.getString("RATING"));
                                }
                            }
                        }
                        AvailableDrivers = stmt.executeQuery("SELECT * FROM DRIVERS WHERE (BusyTill IS NULL) OR (now() > BusyTill)");
                        while(AvailableDrivers.next()){
                            if(distance(lat1,Double.parseDouble(AvailableDrivers.getString("LATITUDE")), long1,Double.parseDouble(AvailableDrivers.getString("LONGITUDE")), 0.0, 0.0)==minDistance){
                                stmt = conn.createStatement();
                                ResultSet ChooseFromThis = stmt.executeQuery("SELECT * FROM DRIVERS WHERE ((BusyTill IS NULL) OR (now() > BusyTill)) AND (RATING="+maxRating+") AND NAME=\""+AvailableDrivers.getString("NAME")+"\"");//busyfree drivers with min distance and max rating
                                if(ChooseFromThis.next()){
                                driverName = ChooseFromThis.getString("NAME");
                                VehicleNum = ChooseFromThis.getString("VEHICLENUM");
                                phoneNum = ChooseFromThis.getString("PHONENUM");
                            	}
                            }
                        }
                        Double inSeconds = (Duration-Duration.intValue())*3600;
                        Double minutes = inSeconds.intValue()/60.0;
                        inSeconds = inSeconds - 60*minutes.intValue();
                        //Booking over
                        if(DriversAvailable){
                            stmt.executeUpdate("UPDATE DRIVERS SET BusyTill=INTERVAL "+Duration.intValue()+" HOUR+INTERVAL "+minutes.intValue()+" MINUTE+INTERVAL "+inSeconds.intValue()+" SECOND+NOW() WHERE NAME=\""+driverName+"\"");//updating busytill for driver
                            stmt.executeUpdate("UPDATE DRIVERS SET LATITUDE="+lat2+",LONGITUDE="+long2+" WHERE NAME=\""+driverName+"\"");//upadating driver location after trip
                            stmt.executeUpdate("UPDATE USERS SET MONEY=MONEY-"+Fare+" WHERE USERID="+userID);//money deduction from user
                            stmt.executeUpdate("UPDATE USERS SET BusyTill=INTERVAL "+Duration.intValue()+" HOUR+INTERVAL "+minutes.intValue()+" MINUTE+INTERVAL "+inSeconds.intValue()+" SECOND+NOW() WHERE USERID="+userID);//updating busytill for driver
                            stmt.executeUpdate("UPDATE DRIVERS SET TRIPS=TRIPS+1 WHERE NAME=\""+driverName+"\"");
                            Summary MyResultsPage = new Summary(driverName,VehicleNum, phoneNum,Fare,Duration.intValue(),minutes.intValue(), maxRating, travelDist);
                            bookingPage.setVisible(false);
                            MyResultsPage.summary.setVisible(true);
                        }
                    }
                    conn.close();
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });
        bookingPage.add(pickupPointText);
        bookingPage.add(pickupPoint);
        bookingPage.add(dropPointText);
        bookingPage.add(walletText);
        bookingPage.add(dropPoint);
        bookingPage.add(walletMoney);
        bookingPage.add(bookButton);
        bookingPage.add(addMoney);
        bookingPage.add(viewBalance);
        bookingPage.setLayout(null);
        bookingPage.setExtendedState(JFrame.MAXIMIZED_BOTH);
        bookingPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //Method to calculate distance in metres based on lat,long
    public static double distance(double lat1, double lat2, double lon1,double lon2, double el1, double el2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
