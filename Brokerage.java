import java.lang.reflect.*;
import java.util.*;


/**
 * Represents a brokerage.
 */
public class Brokerage implements Login
{
    private Map<String, Trader> traders;

    private Set<Trader> loggedTraders;

    private StockExchange exchange;


    //
    // The following are for test purposes only
    //

    /**
     * Gets traders
     * 
     * @return traders the traders
     */
    protected Map<String, Trader> getTraders()
    {
        return traders;
    }


    /**
     * Gets logged in traders
     * 
     * @return loggedTraders traders that are logged in
     */
    protected Set<Trader> getLoggedTraders()
    {
        return loggedTraders;
    }


    /**
     * Gets exchange
     * 
     * @return exchange exchange
     */
    protected StockExchange getExchange()
    {
        return exchange;
    }


    /**
     * Tries to register a new trader with a given screen name and password.
     * Creates a user based on the givne username and password.
     * 
     * @param name
     *            The username
     * @param password
     *            The password
     * 
     * @return Returns -1 if the username doesn't meet the length requirements,
     *         returns -2 if the password doesn't meet the length requirements,
     *         returns -3 if the username is take, returns 0 if successful.
     */
    public int addUser( String name, String password )
    {
        if ( name.length() < 4 || name.length() > 10 )
        {
            return -1;
        }
        if ( password.length() < 2 || password.length() > 10 )
        {
            return -2;
        }
        if ( traders.containsKey( name ) )
        {
            return -3;
        }

        Trader user = new Trader( this, name, password );
        traders.put( name, user );
        return 0;

    }


    /**
     * Gets the quote for a symbol the trader requested.
     * 
     * @param symbol
     *            The symbol to get the quote
     * @param trader
     *            Which tradr to show the quote.
     */
    public void getQuote( String symbol, Trader trader )
    {
        trader.recieveMessage( exchange.getQuote( symbol ) );
    }


    /**
     * Logs in the user by displaying the message "Welcome to SafeTrade!" and
     * adding the user to list of logged users.
     * 
     * 
     * @param name
     *            The username
     * @param password
     *            The password
     * @return If there's no such user, return -1 If the password doesn't match,
     *         return -2 If the user is already logged in, return -3. Returns 0
     *         if succesfull.
     */
    public int login( String name, String password )
    {
        Trader user = traders.get( name );

        if ( !traders.containsKey( name ) )
        {
            return -1;
        }
        if ( !user.getPassword().equals( password ) )
        {
            return -2;
        }
        if ( loggedTraders.contains( name ) )
        {
            return -3;
        }

        user.recieveMessage( "Welcome to SafeTrade!" );
        user.openWindow();
        loggedTraders.add( user );

        return 0;
    }


    /**
     * Logsout the trader
     * 
     * @param trader
     *            to log out
     */
    public void logout( Trader trader )
    {
        loggedTraders.remove( trader );
    }


    /**
     * Places an order based on the traders request If the order doesn't exist,
     * exit the method.
     * 
     * @param order
     *            What to order
     */
    public void placeOrder( TradeOrder order )
    {
        if ( order == null )
        {
            return;
        }

        exchange.placeOrder( order );
    }


    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that
     * superclass fields are left out of this implementation.
     * </p>
     * 
     * @return a string representation of this Brokerage.
     */
    public String toString()
    {
        String str = this.getClass().getName() + "[";
        String separator = "";

        Field[] fields = this.getClass().getDeclaredFields();

        for ( Field field : fields )
        {
            try
            {
                str += separator + field.getType().getName() + " " + field.getName() + ":"
                    + field.get( this );
            }
            catch ( IllegalAccessException ex )
            {
                System.out.println( ex );
            }

            separator = ", ";
        }

        return str + "]";
    }
}
