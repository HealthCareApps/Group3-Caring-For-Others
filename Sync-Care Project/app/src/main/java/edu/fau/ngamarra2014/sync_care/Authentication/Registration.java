package edu.fau.ngamarra2014.sync_care.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.HomeActivity;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.PatientListActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class Registration extends Activity {
    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, null, null, 2);

    EditText inputFirst, inputLast, inputEmail, inputUsername, inputPassword;
    Button register;
    RadioGroup account;
    RadioButton type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        inputFirst = (EditText) findViewById(R.id.firstname);
        inputLast = (EditText) findViewById(R.id.lastname);
        inputEmail = (EditText) findViewById(R.id.regemail);
        inputUsername = (EditText) findViewById(R.id.regusername);
        inputPassword = (EditText) findViewById(R.id.regpass);

        register = (Button) findViewById(R.id.register);
        account = (RadioGroup) findViewById(R.id.account);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FormValidation((ViewGroup) findViewById(R.id.registration))){
                    type = (RadioButton) findViewById(account.getCheckedRadioButtonId());
                    user.setUsername(inputUsername.getText().toString());
                    user.setPassword(inputPassword.getText().toString());
                    user.setFirst(inputFirst.getText().toString());
                    user.setLast(inputLast.getText().toString());
                    user.setEmail(inputEmail.getText().toString());
                    user.setAccountType(type.getText().toString());
                    new CreateNewUser().execute();
                }
            }
        });

    }

    private boolean FormValidation(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                if (((EditText)view).getText().toString().length() == 0){
                    ((EditText) view).setError(((EditText) view).getHint() + " is required!");
                    return false;
                }
            }
        }
        return true;
    }

    class CreateNewUser extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        private String register_caretaker_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Authentication/register.php";

        private JSONObject response;

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("first", user.getFirst());
            query.add("last", user.getLast());
            query.add("email", user.getEmail());
            query.add("username", user.getUsername());
            query.add("password", user.getPassword());
            query.add("account", user.getAccountType());

            jsonParser.setParams(query);
            response = jsonParser.makeHttpRequest(register_caretaker_url, "POST");

            return null;
        }

        protected void onPostExecute(String file_url) {
            try {
                if (response.has("Successful")) {
                    user.setID(response.getInt("id"));
                    dbHandler.addUser(user);
                    if(user.getAccountType().equals("Caretaker"))
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    else
                        startActivity(new Intent(getApplicationContext(), PatientListActivity.class));
                    finish();
                }else if(response.has("Error")){
                    inputUsername.setError("Username already exists");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }

}
