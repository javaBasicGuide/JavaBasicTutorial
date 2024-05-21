import java.sql.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        //példányositás
        String[] params = args;
        //metodus meghivás
        new App().MyFileReader(params);
    }

    public void MyFileReader(String[] params) {

        //array példányositás
        ArrayList<kolcsonzesek> kolcsonzesekArray = new ArrayList<kolcsonzesek>();
        
        try {  //elso file be olvasasa

            File myObj = new File(params[0]);
            
            Scanner myReader = new Scanner(myObj, "utf-8");
            
            myReader.nextLine(); // elso sor pasz
            
            while (myReader.hasNextLine()) { // file sorol sora be olvasása az listába be
                String data = myReader.nextLine();
                
                String[] myArray = data.split(";"); 
                
                // tomb készitése
                kolcsonzesek kolcsonzesek = new kolcsonzesek(Integer.parseInt(myArray[0]), myArray[1], myArray[2], myArray[3]);
                kolcsonzesekArray.add(kolcsonzesek);
                
                
            }
            myReader.close();

        } catch (FileNotFoundException e) { // ha nincs meg a file
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //---------------------------------------------------------------------------------------------------------------------------------------

        ArrayList<kolcsonzok> kolcsonzokArray = new ArrayList<kolcsonzok>();

        try {
            
            File myObj = new File(params[1]);
            
            Scanner myReader = new Scanner(myObj, "utf-8");
            
            myReader.nextLine();
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                
                String[] myArray2 = data.split(";");
                
                kolcsonzok kolcsonzok = new kolcsonzok(myArray2[0], myArray2[1]);
                kolcsonzokArray.add(kolcsonzok);
                
                
            }
            myReader.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        uploadData(kolcsonzokArray, kolcsonzesekArray);

    }
//-------------------------------------------------------------------------------------------------------------------

    public void uploadData( ArrayList<kolcsonzok> kolcsonzokArray , ArrayList<kolcsonzesek> kolcsonzesekArray  ){
        
        // adat bazis csatlakozas
        Connection conn = null;
        //https://mariadb.com/downloads/connectors/connectors-data-access/java8-connector/
        String url = "jdbc:mariadb://localhost:3306/konyvtar"; // adatbazis
        try {
            conn = DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            System.out.println("hibás adatbazis csatlakozás");
        }
//---------------------------------------------------------------------

try {
    Statement stmt = conn.createStatement(); //példanyositas
    for (kolcsonzok kolcsonzok : kolcsonzokArray) { 
        String sql = "INSERT INTO kolcsonzok (nev, szuletesi_datum) VALUES ('"+kolcsonzok.name+"','"+kolcsonzok.birthdate+"');";  // tomb feltoltese 

        stmt.executeQuery(sql); 
    }
           System.out.println("kolcsonzok sikeresen feltoltve");

        } catch (SQLException e) {
            System.out.println("nem sikerult az sql statmentet végre hajtani");
        }
//----------------------------------------------------------------------

        try {
            Statement stmt = conn.createStatement();
            for (kolcsonzesek kolcsonzesek : kolcsonzesekArray) {
                String sql = "INSERT INTO kolcsonzesek (kolcsonzo_id, iro, mufaj, cim) VALUES ("+kolcsonzesek.kolcsonzoId+",'"+kolcsonzesek.iro+"','"+kolcsonzesek.mufaj+"','"+kolcsonzesek.cim+"');"; 
                stmt.executeQuery(sql);  
            }
            System.out.println("kolcsonzesek sikeresen feltoltve");
        
        } catch (Exception e) {
            System.out.println("nem sikerult az sql statmentet végre hajtani");
        }

    }
}
