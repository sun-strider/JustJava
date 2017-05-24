/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        // Check if quantity already at 100
        if (quantity == 100) {
            // Show a toast error message if already 100
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }

        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        // Check if quantity already at 1
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }

        quantity = quantity - 1;
        display(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        // Get the name which is input into EditText field
        EditText nameInput = (EditText) findViewById(R.id.name_input);
        String userName = nameInput.getText().toString();

        // Check if user wants whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Check if user wants chocolate topping
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // calulate price
        int price = calculatePrice(quantity, hasWhippedCream, hasChocolate);

        // Display the order summary on the screen
        String priceMessage = createOrderSummary(userName, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"justjava@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, userName));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param quantity        is the number of cups of coffee ordered
     * @param addWhippedCream is true if whipped cream is added
     * @param addChocolate    is true if chocolate is added
     */
    private int calculatePrice(int quantity, boolean addWhippedCream, boolean addChocolate) {

        int pricePerCoffee = 5;

        // Check if additional whipped cream is added. if true, add 1$ to the price of each coffee
        if (addWhippedCream) {
            pricePerCoffee += 1;
        }
        // Check if additional chocolate is added. if true, add 1$ to the price of each coffee
        if (addChocolate) {
            pricePerCoffee += 2;
        }
        return quantity * pricePerCoffee;
    }

    /**
     * Creates a summary of an order
     *
     * @param name            is the name of the customer
     * @param priceOfOrder    is the price of the whole order
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate    is whether or not the user wants whipped cream topping
     * @return the text of the summary
     */
    private String createOrderSummary(String name, int priceOfOrder, boolean addWhippedCream, boolean addChocolate) {

        // Change toppings info from true/false to yes/no
        String withWhippedCream = changeBoolToYesOrNo(addWhippedCream);
        String withChocolate = changeBoolToYesOrNo(addChocolate);

        String priceMessage =
                getString(R.string.order_summary_name, name) + "\n" +
                        getString(R.string.order_summary_whipped_cream, withWhippedCream) + "\n" +
                        getString(R.string.order_summary_chocolate, withChocolate) + "\n" +
                        getString(R.string.order_summary_quantity, quantity) + "\n" +
                        getString(R.string.order_summary_price, priceOfOrder) + "\n" +
                        getString(R.string.thank_you);
        return priceMessage;
    }

    private String changeBoolToYesOrNo(boolean binaryStatement) {
        String answer = getString(R.string.answer_no);
        if (binaryStatement) {
            answer = getString(R.string.answer_yes);
        }
        return answer;
    }

    /**
     * This method displays the given quantity value on the screen.
     * (TextView) is casting findViewById to return a TextView (instead of View, which would be the normal return type)
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}