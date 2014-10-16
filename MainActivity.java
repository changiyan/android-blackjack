package com.example.blackjack;

import java.util.Random;

import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity {
	private final static Random random = new Random(); // for Randoms
	private static final String GAMES = "games";
	private final static int win = 1;
	private final static int lose = 2;
	private final static int push = 3;
	private final static int blackjack = 4;
	private final static int surrender = 5;
	private final static int bust = 6;
	// bust, push, blackjack
	private int amount = 1000; // starting amount
	private int bet = 0; // default bet
	private int fbet = 0; // final bet, don't want them changing partway
	private int pCard = 0; // for ImageView
	private int dCard = 0; // for ImageView
	private int cnum = 0; // each draw increments card number
	private int pScore = 0;
	private int dScore = 0;
	
	private TextView mAmountTextView; // amount player has
	private TextView bAmountTextView; // bet amount
	private TextView dScoreTextView; // text view for dealer score
	private TextView pScoreTextView; // text view for player score
	private ImageView d1ImageView;
	private ImageView d2ImageView;
	private ImageView d3ImageView;
	private ImageView d4ImageView;
	private ImageView d5ImageView;
	private ImageView p1ImageView;
	private ImageView p2ImageView;
	private ImageView p3ImageView;
	private ImageView p4ImageView;
	private ImageView p5ImageView;
	
	private SharedPreferences savedGame; // saved game
	
	 private final static int[] cardDeck = new int[] {   R.drawable.sa,
		 R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5, R.drawable.s6,
		 R.drawable.s7, R.drawable.s8, R.drawable.s9, R.drawable.s10, R.drawable.sj,
		 R.drawable.sq, R.drawable.sk, R.drawable.ca, R.drawable.c2, R.drawable.c3,
		 R.drawable.c4, R.drawable.c5, R.drawable.c6, R.drawable.c7, R.drawable.c8,
		 R.drawable.c9, R.drawable.c10, R.drawable.cj, R.drawable.cq, R.drawable.ck,
		 R.drawable.da, R.drawable.d2, R.drawable.d3, R.drawable.d4, R.drawable.d5,
		 R.drawable.d6, R.drawable.d7, R.drawable.d8, R.drawable.d9, R.drawable.d10, 
		 R.drawable.dj, R.drawable.dq, R.drawable.dk, R.drawable.ha, R.drawable.h2, 
		 R.drawable.h3, R.drawable.h4, R.drawable.h5, R.drawable.h6, R.drawable.h7, 
		 R.drawable.h8, R.drawable.h9, R.drawable.h10, R.drawable.hj, R.drawable.hq, 
		 R.drawable.hk}; // deck of cards
	 
	 private int[] pHand = new int[] {0,0,0,0,0};
	 private int[] dHand = new int[] {0,0,0,0,0};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		savedGame = getSharedPreferences(GAMES, MODE_PRIVATE);
		
		mAmountTextView = (TextView) findViewById(R.id.mAmountTextView);
		mAmountTextView.setText(""+amount);
		
		bAmountTextView = (TextView) findViewById(R.id.bAmountTextView);
		bAmountTextView.setText(""+bet);
		
		dScoreTextView = (TextView) findViewById(R.id.dScoreTextView);
		pScoreTextView = (TextView) findViewById(R.id.pScoreTextView);
		d1ImageView = (ImageView) findViewById(R.id.d1ImageView);
		d2ImageView = (ImageView) findViewById(R.id.d2ImageView);
		d3ImageView = (ImageView) findViewById(R.id.d3ImageView);
		d4ImageView = (ImageView) findViewById(R.id.d4ImageView);
		d5ImageView = (ImageView) findViewById(R.id.d5ImageView);
		p1ImageView = (ImageView) findViewById(R.id.p1ImageView);
		p2ImageView = (ImageView) findViewById(R.id.p2ImageView);
		p3ImageView = (ImageView) findViewById(R.id.p3ImageView);
		p4ImageView = (ImageView) findViewById(R.id.p4ImageView);
		p5ImageView = (ImageView) findViewById(R.id.p5ImageView);
		
		Button hitButton = (Button) findViewById(R.id.hitButton);
	    hitButton.setOnClickListener(hitButtonListener);
	    
	    Button standButton = (Button) findViewById(R.id.standButton);
	    standButton.setOnClickListener(standButtonListener);
	    
	    Button surrenderButton = (Button) findViewById(R.id.surrenderButton);
	    surrenderButton.setOnClickListener(surrenderButtonListener);
	    
	    Button resetButton = (Button) findViewById(R.id.resetButton);
	    resetButton.setOnClickListener(resetButtonListener);
	    Button submitButton = (Button) findViewById(R.id.submitButton);
	    submitButton.setOnClickListener(submitButtonListener);
	    
	    Button saveButton = (Button) findViewById(R.id.saveButton);
	    saveButton.setOnClickListener(saveButtonListener);
	    Button loadButton = (Button) findViewById(R.id.loadButton);
	    loadButton.setOnClickListener(loadButtonListener);
	    
	    ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
	    imageButton1.setOnClickListener(imageButton1Listener);
	    ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
	    imageButton2.setOnClickListener(imageButton2Listener);
	    ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
	    imageButton3.setOnClickListener(imageButton3Listener);
	    ImageButton imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
	    imageButton4.setOnClickListener(imageButton4Listener);
	    ImageButton imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
	    imageButton5.setOnClickListener(imageButton5Listener);
	    
	    newgame(); // start a new game
	}
	
	public void newgame(){
		pCard = dCard = pScore = dScore = bet = fbet = 0; // clear cards+score
		
		dScoreTextView.setText(""+dScore); // Display default values
		pScoreTextView.setText(""+pScore); // Display default values
		bAmountTextView.setText(""+bet);
		
		for(int i = 0; i < 5; i++){
			dHand[i] = 0;
			pHand[i] = 0;
		} // clear hands + change images
		p1ImageView.setImageResource(R.drawable.b2fv);
		p2ImageView.setImageResource(R.drawable.b2fv);
		p3ImageView.setImageResource(R.drawable.b2fv);
		p4ImageView.setImageResource(R.drawable.b2fv);
		p5ImageView.setImageResource(R.drawable.b2fv);
		
		d1ImageView.setImageResource(R.drawable.b1fv);
		d2ImageView.setImageResource(R.drawable.b1fv);
		d3ImageView.setImageResource(R.drawable.b1fv);
		d4ImageView.setImageResource(R.drawable.b1fv);
		d5ImageView.setImageResource(R.drawable.b1fv);
		
		shuffle();
	} // reset everything for new game
	
	public void shuffle(){
		for(int i = 0; i < 52; i++){
			int j = random.nextInt(52), temp;
			temp = cardDeck[i];
			cardDeck[i] = cardDeck[j];
			cardDeck[j] = temp;
		}
	} // shuffle all the cards
	
	int updateP(){
		int value = 0;
		boolean ace = false;
		for(int i = 0; i < pCard; i++){
			int card = getValue(pHand[i]);
			if(card == 1){
				ace = true;
			}
			value += card;
		} // only reason calculating each time is ace
		if(value < 12 && ace)
			value += 10;
		switch(pCard){
		case 5: p5ImageView.setImageResource(pHand[4]);
		case 4: p4ImageView.setImageResource(pHand[3]);
		case 3: p3ImageView.setImageResource(pHand[2]);
		case 2: p1ImageView.setImageResource(pHand[0]);
			p2ImageView.setImageResource(pHand[1]);		
		} // update imageview
		
		pScoreTextView.setText(""+value);
		return value;
	}// player set card/score image, gets score
	
	int updateD(){
		int value = 0;
		boolean ace = false;
		for(int i = 0; i < dCard; i++){
			int card = getValue(dHand[i]);
			if(card == 1){
				ace = true;
			}
			value += card;
		}
		if(value < 12 && ace)
			value += 10;
		
		switch(dCard){
		case 5: d5ImageView.setImageResource(dHand[4]);
		case 4: d4ImageView.setImageResource(dHand[3]);
		case 3: d3ImageView.setImageResource(dHand[2]);
		case 2: d2ImageView.setImageResource(dHand[1]);
		case 1:	d1ImageView.setImageResource(dHand[0]);
		}
		
		dScoreTextView.setText(""+value);
		return value;
	}// dealer set card/score image, gets score
	
	int getValue(int card){
		if(card == R.drawable.sa || card == R.drawable.da ||
				card == R.drawable.ha || card == R.drawable.ca){
			return 1;
		}
		if(card == R.drawable.s2 || card == R.drawable.d2 ||
				card == R.drawable.h2 || card == R.drawable.c2){
			return 2;
		}
		if(card == R.drawable.s3 || card == R.drawable.d3 ||
				card == R.drawable.h3 || card == R.drawable.c3){
			return 3;
		}
		if(card == R.drawable.s4 || card == R.drawable.d4 ||
				card == R.drawable.h4 || card == R.drawable.c4){
			return 4;
		}
		if(card == R.drawable.s5 || card == R.drawable.d5 ||
				card == R.drawable.h5 || card == R.drawable.c5){
			return 5;
		}
		if(card == R.drawable.s6 || card == R.drawable.d6 ||
				card == R.drawable.h6 || card == R.drawable.c6){
			return 6;
		}
		if(card == R.drawable.s7 || card == R.drawable.d7 ||
				card == R.drawable.h7 || card == R.drawable.c7){
			return 7;
		}
		if(card == R.drawable.s8 || card == R.drawable.d8 ||
				card == R.drawable.h8 || card == R.drawable.c8){
			return 8;
		}
		if(card == R.drawable.s9 || card == R.drawable.d9 ||
				card == R.drawable.h9 || card == R.drawable.c9){
			return 9;
		}
		if(card == R.drawable.s10 || card == R.drawable.d10 ||
				card == R.drawable.h10 || card == R.drawable.c10 ||
				card == R.drawable.dj || card == R.drawable.sj ||
				card == R.drawable.hj || card == R.drawable.cj ||
				card == R.drawable.dq || card == R.drawable.sq ||
				card == R.drawable.hq || card == R.drawable.cq ||
				card == R.drawable.dk || card == R.drawable.sk ||
				card == R.drawable.hk || card == R.drawable.ck){
			return 10;
		}
		return 0; // if card isn't one of these, its an unflipped card
	} // return card value
	
	public void set(int val){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		switch(val){
		case 1: amount += fbet; builder.setMessage(R.string.win);break; // win
		case 2: amount -= fbet; builder.setMessage(R.string.lose);break; // lose
		case 3: builder.setMessage(R.string.push); break; // push
		case 4: amount += fbet*1.5; builder.setMessage(R.string.bj); break; // BlackJack
		case 5: amount -= fbet/2; builder.setMessage(R.string.surrendered); break; // surrender
		case 6: amount -= fbet; builder.setMessage(R.string.bust); break; // bust, need for dialog
		}
		mAmountTextView.setText(""+amount);
		
		builder.setPositiveButton(getString(R.string.ng),
				new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				if(amount <= 0)
					amount = 1000; // new game
				newgame();
			} // cancel button to cancel dialog
		});
		
		builder.create().show(); // display AlertDialog
	} // sets wins and losses, scores
	
	// Listener for hit button
	public OnClickListener hitButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if(pCard < 5 && pCard > 1){
				pHand[pCard++] = cardDeck[cnum++];
				pScore = updateP();
			}
			if(pScore > 21){
				set(bust);
			}
		}
	}; // check is to make sure that the game has started
	
	public OnClickListener standButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if(dCard > 0){
				while(dScore < 17 && dCard < 5){
					dHand[dCard++] = cardDeck[cnum++];
					dScore = updateD();
				} // will need 2 cards to reach 17 no matter what
				if(dScore > 21 || dScore < pScore){
					set(win);
				}
				else if(dScore > pScore){
					set(lose);
				}
				else
					set(push);
			}
		}
	}; // same check
	
	public OnClickListener surrenderButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			set(surrender);
		}
	}; // no check because won't do anything bad pre-game
	
	public OnClickListener saveButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			SharedPreferences.Editor preferencesEditor = savedGame.edit();
			preferencesEditor.putInt("Amount", amount);
			preferencesEditor.putInt("Final", fbet);
			preferencesEditor.putInt("pCard", pCard);
			preferencesEditor.putInt("dCard", dCard);
			preferencesEditor.putInt("phand1", pHand[0]);
			preferencesEditor.putInt("phand2", pHand[1]);
			preferencesEditor.putInt("phand3", pHand[2]);
			preferencesEditor.putInt("phand4", pHand[3]);
			preferencesEditor.putInt("phand5", pHand[4]);
			preferencesEditor.putInt("dhand1", dHand[0]);
			preferencesEditor.putInt("dhand2", dHand[1]);
			preferencesEditor.putInt("dhand3", dHand[2]);
			preferencesEditor.putInt("dhand4", dHand[3]);
			preferencesEditor.putInt("dhand5", dHand[4]);
			for(int i=0;i<52;i++)  
		    {
		        preferencesEditor.putInt("deck_" + i, cardDeck[i]);  
		    }

			preferencesEditor.apply();
		}
	}; //save game, only amount
	
	public OnClickListener loadButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			newgame();
			amount = savedGame.getInt("Amount", 1000);
			fbet = bet = savedGame.getInt("Final", 0);
			pCard = savedGame.getInt("pCard", 0);
			dCard = savedGame.getInt("dCard", 0);
			pHand[0] = savedGame.getInt("phand1", 0);
			pHand[1] = savedGame.getInt("phand2", 0);
			pHand[2] = savedGame.getInt("phand3", 0);
			pHand[3] = savedGame.getInt("phand4", 0);
			pHand[4] = savedGame.getInt("phand5", 0);
			dHand[0] = savedGame.getInt("dhand1", 0);
			dHand[1] = savedGame.getInt("dhand2", 0);
			dHand[2] = savedGame.getInt("dhand3", 0);
			dHand[3] = savedGame.getInt("dhand4", 0);
			dHand[4] = savedGame.getInt("dhand5", 0);
			for(int i=0;i<52;i++)  
		    {
		       cardDeck[i] = savedGame.getInt("deck_" + i, cardDeck[i]);  
		    } // if it's not existing, get the card deck now.
			pScore = updateP();
			dScore = updateD();
			mAmountTextView.setText(""+amount);
			bAmountTextView.setText(""+bet);
		}
	}; // load game
	
	public OnClickListener resetButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			bet = 0;
			bAmountTextView.setText(""+bet);
		}
	}; // reset bet
	
	public OnClickListener submitButtonListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if(pCard == 0){
				fbet = bet;
			
			pHand[pCard++] = cardDeck[cnum++];
		    pHand[pCard++] = cardDeck[cnum++];
		    // Draw 2 cards for player
		    dHand[dCard++] = cardDeck[cnum++];
		    // Draw 1 card for dealer
		    pScore = updateP();
		    dScore = updateD();
		    if (pScore == 21){
		    	dHand[dCard++] = cardDeck[cnum++];
				dScore = updateD();
				if(dScore != 21)
					set(blackjack); // Black Jack
				else
					set(push); // push
		    }
			}
		}
	}; // submit game + start game
	
	public OnClickListener imageButton1Listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if((bet+1) <= amount){
				bet += 1;
				bAmountTextView.setText(""+bet);
			}
		}
	}; // increase bet by 1
	
	public OnClickListener imageButton2Listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if((bet+5) <= amount){
				bet += 5;
				bAmountTextView.setText(""+bet);
			}
		}
	}; // increase bet by 5
	
	public OnClickListener imageButton3Listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if((bet+25) <= amount){
				bet += 25;
				bAmountTextView.setText(""+bet);
			} 
		}
	}; // increase bet by 25
	
	public OnClickListener imageButton4Listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if((bet+100) <= amount){
				bet += 100;
				bAmountTextView.setText(""+bet);
			}
		}
	}; // increase bet by 100
	
	public OnClickListener imageButton5Listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			if((bet+500) <= amount){
				bet += 500;
				bAmountTextView.setText(""+bet);
			}
		}
	}; // increase bet by 500
	
}
