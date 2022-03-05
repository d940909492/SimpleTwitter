package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.util.Log
import okhttp3.Headers
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.TweetComposeText)
        btnTweet = findViewById(R.id.TweetButton)

        client = TwitterApplication.getRestClient(this)

        val etValue = findViewById(R.id.TweetComposeText) as EditText

        val TextCounter = findViewById<TextView>(R.id.CountText)

        etValue.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                TextCounter.setText((s.toString().length).toString())
            }
        })

        btnTweet.setOnClickListener {

            val tweetContent = etCompose.text.toString()

            if(tweetContent.isEmpty()){
                Toast.makeText(this,"Is Empty",Toast.LENGTH_SHORT).show()
            }
            else if (tweetContent.length > 150){
                Toast.makeText(this,"Too long, no longer than 150",Toast.LENGTH_SHORT).show()
            }else {
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {

                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG,"Success")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK,intent)
                        finish()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed", throwable)
                    }

                })
            }
        }
        }
    companion object{
        val TAG = "ComposeActivity"
    }
}