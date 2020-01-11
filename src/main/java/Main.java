import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main
{
    public static void main(String[] args) {
        StringBuilder db_url = new StringBuilder();
        db_url.append("jdbc:mysql://")
                .append("localhost:")
                .append("3306/")
                .append("skillbox?")
                .append("user=root&")
                .append("password=skillbox");
        try {
            Connection connection = DriverManager.getConnection(db_url.toString());
            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("select course_name, subscription_date from purchaselist");
            ResultSet resultSet = statement.executeQuery("select course_name, avg(count_subscription) as avg_qtty " +
                    "from " +
                    "(select " +
                    "course_name, " +
                    "month(subscription_date) as month_short, " +
                    "count(*) as count_subscription " +
                    "from purchaselist " +
                    "group by course_name, month_short " +
                    "order by course_name, month_short) subscrtioptionlist " +
                    "group by course_name " +
                    "order by course_name");

            System.out.printf("%-36s%-20s","Название курса","Среднее кол-во покупки");
            System.out.println();
            System.out.println("---------------------------------   ----------------------");
            while (resultSet.next())
            {
                String courseName = resultSet.getString("course_name");
                String avg_purchases = resultSet.getString("avg_qtty");

                System.out.printf("%-36s%-20s", courseName, avg_purchases);
                System.out.println();
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
