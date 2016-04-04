package hsyn.caliskan.tweetme;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        final Button buttonTweet = (Button)findViewById(R.id.button_tweet);
        final EditText editTextTweet = (EditText)findViewById(R.id.editText_tweet);

        editTextTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(editTextTweet.getText().toString().length()>140)
                {buttonTweet.setEnabled(false); editTextTweet.setTextColor(Color.RED);}
                else
                {buttonTweet.setEnabled(true);editTextTweet.setTextColor(Color.BLACK);}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendTweet().execute(editTextTweet.getText().toString());
                editTextTweet.getText().clear();
            }
        });
    }
    private class SendTweet extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Constants.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
            AccessToken accessToken = new AccessToken(Constants.Access_Token, Constants.Access_Token_Secret);
            final Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            try {
                twitter4j.Status response = twitter.updateStatus(params[0]);
                return response.toString();
            } catch (TwitterException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null)
                Toast.makeText(MainActivity.this, "Tweet gönderilemedi.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, "Tweet gönderildi.", Toast.LENGTH_SHORT).show();

        }

    }
}
