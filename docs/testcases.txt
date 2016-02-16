package cpatel1.test;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by chaitali on 16-02-08.
 */

/* so far variables
 * ArtList = ALL THE ART w/ type array
 * allArt = list w/ type ArtList
 * Artwork = type for all art eg. specifies qualities
 * art = type Artwork each individual piece of Art
 */

public class ArtListTest extends ActivityInstrumentationTestCase2 {

    public ArtListTest() {
        super(MainActivity.class);
    }

    //The Items are in ArtList (ArrayList<ArtList>)
    //Note: Each Artwork has the attributes (title, artist, year, dimensions, owner, borrower, minbid, status)
    //but for simplicity has been shortened to (title, artist, status)


    // US 01.01.01 Test Adding Items to Owner Listings
    public void testAddItem(){

        ArtList myArt = new ArtList();
        // Assumes owner is logged in and status is set to available, with no borrower
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available");
        myArt.add(art);

        //check if allArt contains the new item
        assertTrue(myArt.hasItem(art));

    }

    // checks for invalid input, should be changed to check each individual field
    // but for testing will check all fields at the same time
    public void testInvalidInput(){
        ArtList myArt = new ArtList();
        Artwork art =new Artwork("", "", "available");
        //check for empty fields
        try{
            myArt.add(art);
        } catch(InvalidArgumentException e){
            displayMessage("Fill empty field!");
        } 
    }

    //US 01.05.01 Testing Deleting Item From Owner Listings
    public void testDeleteItem(){

        ArtList myArt = new ArtList();
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available");
        myArt.add(art);
        //check if actually added to listings
        assertTrue(myArt.hasItem(art));

        myArt.delete(art);
        //check if item actually deleted
        assertFalse(myArt.hasItem(art));

        //check multiple deletes
        Artwork art2 = new Artwork("The Dream", "Edvard Munch", "available");
        Artwork art3 = new Artwork("The Seam", "Edvard Munch", "available");
        myArt.add(art2);
        myArt.add(art3);
        //check if actually added to listings
        assertTrue(myArt.hasItem(art2));
        assertTrue(myArt.hasItem(art3));
        //delete item
        myArt.delete(art2);
        myArt.delete(art3);
        assertFalse(myArt.hasItem(art2));
        assertFalse(myArt.hasItem(art3));





    }

    // US 01.04.01 Editing Item in Owner Listings
    public void testEditItem(){

        ArtList myArt = new ArtList();
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available");
        myArt.add(art);
        //check if added to listings
        assertTrue(myArt.hasItem(art));
        //load item from myArt
        Artwork returnedArt = myArt.getItem(0);
        //change title of item
        returnedArt.setTitle("Starry Night");
        assertEquals(returnedArt.getTitle(), "Starry Night");

    }



    //US 01.02.01
    //As an owner, I want to view a list of all my things, and their descriptions and statuses.
    public void testGetListings(){

        //each account will have a (username, password?, email address, city, phone #)
        //for right now shortened to (username, email, city)
        ArtList myArt = new ArtList();
        UserAccount myAccount = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");

        Artwork art = new Artwork("Mona Lisa", "Leonardo da Vinci", "available");
        Artwork art1 = new Artwork("The Scream", "Edvard Munch", "available");

        // add art to listings
        myArt.add(art);
        myArt.add(art1);
        //check if added to listings
        assertTrue(myArt.hasItem(art));
        assertTrue(myArt.hasItem(art1));
        //connect myArt to owner account
        myAccount.setListing(myArt);
        //check the listings exist with account (not empty)
        assertTrue(myAccount.getListing() != null);

        //check if owner, description and status are correctly shown
        assertEquals(myAccount.getListing().getItem(1).getTitle(), "The Scream");
        assertEquals(myAccount.getListing().getItem(1).getArtist(), "Edvard Munch");
        assertEquals(myAccount.getListing().getItem(1).getStatus(), "available");

    }

    // US 01.03.01
    // As an owner, I want to view one of my things, its description and status.
    public void testGetItem(){


        ArtList myArt = new ArtList();
        UserAccount myAccount = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        //add to owner account
        Artwork art = new Artwork("Mona Lisa", "Leonardo da Vinci", "available");
        myArt.add(art);

        //check if added to listings
        assertTrue(myArt.hasItem(art));
        //connect myArt to owner account
        myAccount.setListing(myArt);
        //check the listings exist with account (not empty)
        assertTrue(myAccount.getListing() != null);
        //check if owner, description and status are correctly shown
        assertEquals(myAccount.getUsername(), "pi314");
        assertEquals(myAccount.getListing().getItem(0).getTitle(), "Mona Lisa");
        assertEquals(myAccount.getListing().getItem(0).getArtist(), "Leonardo da Vinci");
        assertEquals(myAccount.getListing().getItem(0).getStatus(), "available");

    }

    // US 02.01.01
    //As an owner or borrower, I want a thing to have a status of one of: available, bidded, or borrowed.
    public void testStatus(){

        //assumes that the user cannot input anything other than borrowed/available/bidded in status
        ArtList myArt = new ArtList();
        ArtList theirArt = new ArtList();

        UserAccount myAccount = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();
		  allAccounts.addAccount(myAccount)
        allAccounts.addAccount(Borrower)

        Artwork art = new Artwork("Mona Lisa", "Leonardo da Vinci", "available");
        myArt.add(art);
        //add to borrower account
        Artwork art1 = new Artwork("The Scream", "Edvard Munch", "bidded");
        theirArt.add(art1);

        //check if added to listings
        assertTrue(myArt.hasItem(art));
        assertTrue(theirArt.hasItem(art1));
        //connect myArt to owner account
        myAccount.setListing(myArt);
        Borrower.setListing(theirArt);
        //change status of item
        myAccount.getListing().getItem(0).setStatus("borrowed");

        //check the listings exist with account (not empty)
        assertTrue(myAccount.getListing() != null);
        //check if owner, description and status are correctly shown
        assertEquals(myAccount.getUsername(), "pi314");
        assertEquals(myAccount.getListing().getItem(0).getStatus(), "borrowed");
        assertEquals(randomAccount.getUsername(), "mercy");
        assertEquals(randomAccount.getListing().getItem(0).getStatus(), "bidded");

    }

    //US 03.01.01
    //As a user, I want a profile with a unique username and my contact information.

    public void testUniqueUser(){

        //add random user into list
        UserAccount account1 = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(account1);
        assertTrue(allAccounts.getAccountsSize() != 0);

        //try creating a new account with same username
        UserAccount myAccount = new UserAccount("pi314", "pi314@hotmail.com", "Edmonton");
        try{
            allAccounts.addAccount(myAccount);
        }catch (AccountExistsException e){
            displayMessage("Choose new username!");
        }

        
    }
  
    //US 03.02.01
    //As a user, I want to edit the contact information in my profile.

    public void testEditUserInfo(){

        //add random user into list
        UserAccount account1 = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(account1);
        //make sure the user is added
        assertTrue(allAccounts.getAccountsSize() != 0);
        //change contact info
        //add error checking for invalid input
        allAccounts.getAccount(0).setCity("Toronto");
        allAccounts.getAccount(0).setEmail("pi@gmail.com");


        //check if it changes
        assertEquals(allAccounts.getAccount(0).getCity(), "Toronto");
        assertEquals(allAccounts.getAccount(0).getEmail(), "pi@gmail.com");
        
    }
  
    //US 03.03.01
    //As a user, I want to, when a username is presented for a thing, retrieve and show its contact information.
    public void testShowUser(){

        //assume user has selected another user of the app,
        //see if correct profile is returned

        UserAccount account1 = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(account1);

        //check if account is added
        assertTrue(allAccounts.hasAccount(account1));

        //try getting account
        assertEquals(allAccounts.getAccount(0).getUsername(), "pi314");
        assertEquals(allAccounts.getAccount(0).getCity(), "Edmonton");
        assertEquals(allAccounts.getAccount(0).getEmail(), "pi@hotmail.com");


    }

    //test validity of input, should be changed to check each field
    public void testInvalidAccount(){

        UserAccount account1 = new UserAccount("", "", "");
        AccountList allAccounts = new AccountList();
        
        try{
            allAccounts.addAccount(account1);
        }catch(InvalidAccountException e){
            displayMessage("Enter valid input!");
        }
        

    }
  
    
  //US 05.01.01
    //As a borrower, I want to bid for an available thing, with a monetary rate (in dollars per day).
    //so try placing a bid on an item
    // if borrowed already error
    //if bid is less than minbid error
    //
    public void testAddBid(){

        //make an account
        UserAccount myAccount = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(myAccount);
        float x = 10;

        //another user with artwork ready to bid on
        UserAccount randomAccount = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        ArtList theirArt = new ArtList();
        Artwork art1 = new Artwork("The Scream", "Edvard Munch", "available");
        theirArt.add(art1);
        randomAccount.setListing(theirArt);

        //make sure it's available/bidded and not borrowed
        assertFalse(randomAccount.getListing().getItem(0).getStatus().equals("borrowed"));

        //try placing a bid of $10 per day on their art as a borrower
        BidList bids = new BidList();
        Bidding bid = new Bidding("pi314", 10);
        // add bid to BidList of item
        bids.addBid(bid);
        //add bid to item's list of bids
        randomAccount.getListing().getItem(0).addBid(bids);

        // check if item had bid on it
        // EDIT THIS: to have less get_____
        assertEquals(randomAccount.getListing().getItem(0).getBids().getBid(0).getBidder(), "pi314");
        assertEquals(randomAccount.getListing().getItem(0).getBids().getBid(0).getRate(), x);


    }

    //US 05.02.01
    //As a borrower, I want to view a list of things I have bidded on that are pending,
    //each thing with its description, owner username, and my bid.

    public void testViewMyBids(){

        UserAccount myAccount = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();

        //another user with artwork ready to bid on
        UserAccount randomAccount = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        ArtList theirArt = new ArtList();
        Artwork art1 = new Artwork("The Scream", "Edvard Munch", "available");
        theirArt.add(art1);
        randomAccount.setListing(theirArt);

        //make sure it's available/bidded and not borrowed
        assertFalse(randomAccount.getListing().getItem(0).getStatus().equals("borrowed"));

        //try placing a bid of $10 per day on their art as a borrower
        BidList bids = new BidList();
        Bidding bid = new Bidding("pi314", 10);
        bids.addBid(bid);
        //add bid to item's list of bids
        randomAccount.getListing().getItem(0).addBid(bids);

        //also add bid to myBids
        ArtList myBids = new ArtList();
        Artwork myBid = randomAccount.getListing().getItem(0);
        myBids.add(myBid);
        myAccount.myBidsPlaced(myBids, randomAccount.getUsername());
        //check if the bid is saved in myBids
        assertEquals(myAccount.getMyBidsPlaced().getItem(0).getTitle(),"The Scream");
        assertEquals(myAccount.getOwner(), "mercy");

    }
  
    //US 05.03.01
    //As an owner, I want to be notified of a bid.

    public void testViewBidNotif(){

        //create new accounts
        UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        //add to account list
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(Borrower);
        allAccounts.addAccount(Owner);
        //add art to owner account
        ArtList myArt = new ArtList();
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available");
        myArt.add(art);
        //check if added to listings
        assertTrue(myArt.hasItem(art));
        //connect myArt to owner account
        Owner.setListing(myArt);
        assertTrue(Owner.getListing() != null);
        //Borrower places bid on The Scream
        BidList bids = new BidList();
        Bidding bid = new Bidding("mercy", 10);
        bids.addBid(bid);
        //add bid to The Scream's list of bids
        Owner.getListing().getItem(0).addBid(bids);
        Owner.getListing().getItem(0).setStatus("bidded");
        //check Owner's Notification List
        assertFalse(Owner.getNotified() != null);

    }

    //US 05.04.01
    //As an owner, I want to view a list of my things with bids.
    public void testViewPendingBids(){

        //create new accounts
        UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        //add to account list
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(Borrower);
        allAccounts.addAccount(Owner);
        //add art to owner account
        ArtList myArt = new ArtList();
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available");
        myArt.add(art);
        //check if added to listings
        assertTrue(myArt.hasItem(art));
        //connect myArt to owner account
        Owner.setListing(myArt);
        assertTrue(Owner.getListing() != null);
        //Borrower places bid on The Scream
        BidList bids = new BidList();
        Bidding bid = new Bidding("mercy", 10);
        bids.addBid(bid);
        //add bid to The Scream's list of bids
        Owner.getListing().getItem(0).addBid(bids);
        Owner.getListing().getItem(0).setStatus("bidded");
        Owner.getListingStatus("bidded");
        assertEquals(Owner.getListingStatus("bidded").getItem(0).getTitle(), "The Scream");

    }

    //US 05.05.01
    //As an owner, I want to view the bids on one of my things.
    public void testViewItemBid(){

        //create new accounts
        UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        UserAccount Borrower2 = new UserAccount("snow", "snow@hotmail.com", "Arctic");
        //add to account list
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(Borrower);
        allAccounts.addAccount(Borrower2);
        allAccounts.addAccount(Owner);
        //add art to owner account
        ArtList myArt = new ArtList();
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available");
        myArt.add(art);
        //check if added to listings
        assertTrue(myArt.hasItem(art));
        //connect myArt to owner account
        Owner.setListing(myArt);
        assertTrue(Owner.getListing() != null);
        //Borrower places bid on The Scream
        BidList bids = new BidList();
        Bidding bid = new Bidding("mercy", 10);
        bids.addBid(bid);
        //add bid to The Scream's list of bids
        Owner.getListing().getItem(0).addBid(bids);
        Owner.getListing().getItem(0).setStatus("bidded");
        //check if the bid is saved in myBids
        assertEquals(Owner.getListing().getItem(0).getBids().getBidCount(), 1);
        //Borrower2 places bid on The Scream
        Bidding bid2 = new Bidding("snow", 100);
        bids.addBid(bid2);
        //add bid to The Scream's list of bids
        Owner.getListing().getItem(0).addBid(bids);
        Owner.getListing().getItem(0).setStatus("bidded");
        assertEquals(Owner.getListing().getItem(0).getBids().getBidCount(), 2);
        assertEquals(Owner.getListing().getItem(0).getBids().getBid(1).getBidder(), "snow");
		  
	}
  
    //US 05.06.01
    //As an owner, I want to accept a bid on one of my things, setting its status to borrowed. (Any other bids are declined.)
    // Use borrower in art attributes
    public void testAcceptBid(){

        //create new accounts
        UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        UserAccount Borrower2 = new UserAccount("snow", "snow@hotmail.com", "Arctic");

        //add to account list
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(Borrower);
        allAccounts.addAccount(Borrower2);
        allAccounts.addAccount(Owner);

        //add art to owner account
        ArtList myArt = new ArtList();
        //Artwork attributes (title, artist, status, borrower)
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available", "");
        myArt.add(art);

        //check if added to listings
        assertTrue(myArt.hasItem(art));

        //connect myArt to owner account
        Owner.setListing(myArt);
        assertTrue(Owner.getListing() != null);

        //Borrower places bid on The Scream
        BidList bids = new BidList();
        Bidding bid = new Bidding("mercy", 10);
        bids.addBid(bid);

        //add bid to The Scream's list of bids
        Owner.getListing().getItem(0).addBid(bids);
        Owner.getListing().getItem(0).setStatus("bidded");
        //check if the bid is saved in myBids
        assertEquals(Owner.getListing().getItem(0).getBids().getBidCount(), 1);
        //Borrower2 places bid on The Scream
        Bidding bid2 = new Bidding("snow", 100);
        bids.addBid(bid2);
        //add bid to The Scream's list of bids
        Owner.getListing().getItem(0).addBid(bids);
        Owner.getListing().getItem(0).setStatus("bidded");
        //accept snow's bid and set to borrowed
        Owner.getListing().getItem(0).setStatus("borrowed");
        Owner.getListing().getItem(0).setBorrower("snow");
        Owner.getListing().getItem(0).getBids().declineAllBids();
        // check if all declined bids deleted
        assertEquals(Owner.getListing().getItem(0).getBids().getBidCount(), 0);



    }

    //US 05.07.01
    //As an owner, I want to decline a bid on one of my things.

    public void testDeclineBid(){

        //create new accounts
        UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");

        //add to account list
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(Borrower);
        allAccounts.addAccount(Owner);

        //add art to owner account
        ArtList myArt = new ArtList();
        //Artwork attributes (title, artist, status, borrower)
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available", "");
        myArt.add(art);

        //check if added to listings
        assertTrue(myArt.hasItem(art));

        //connect myArt to owner account
        Owner.setListing(myArt);
        assertTrue(Owner.getListing() != null);

        //Borrower place bid on The Scream
        BidList bids = new BidList();
        Bidding bid = new Bidding("mercy", 10);
        bids.addBid(bid);

        //add bid to The Scream's list of bids
        Owner.getListing().getItem(0).addBid(bids);
        Owner.getListing().getItem(0).setStatus("bidded");

        //check if the bid is saved in myBids
        assertEquals(Owner.getListing().getItem(0).getBids().getBidCount(), 1);

        //decline single bid
        Owner.getListing().getItem(0).getBids().declineBid(0);
        assertEquals(Owner.getListing().getItem(0).getBids().getBidCount(), 0);

    }
  
    // US 04.01.01
    // As a borrower, I want to specify a set of keywords, and search for all things not 
    // currently borrowed whose description contains all the keywords. 
    // These search results should show each thing’s description, owner username, and status.
    public void testViewSearchItems() {
    	  UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();
        allAccounts.add(Borrower);
        allAccounts.add(Owner);
      
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available", "");
        ArtList.myArt = new Artlist();
        myArt.add(art);
        myArt.getItem(art).addDescription("This item is expensive.");
        
        // connect myArt to owner account
        Owner.setListing(myArt);
        assertTrue(Owner.getListing() != null);
        
        // set a keyword to search for
        String keyword = new String();
        keyword = "expensive";
      
        // set an expect return
        ArrayList<Artwork> expectedList = new ArrayList();
        expectedList.add(art);
      
        // assert that we get expectedList back from function call
        assertEquals(expectedList, ViewSearchItems(keyword));
    }
  
  
    // US 06.01.01
    // As a borrower, I want to view a list of things I am borrowing, each thing with its description and owner username.
    public void testBorrowedList() {
	// Create two owners and one borrower
	UserAccount Owner1 = new UserAccount("snow", "snow@hotmail.com", "Arctic");
	UserAccount Owner2 = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
	UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");

	//add to account list
	AccountList allAccounts = new AccountList();
	allAccounts.addAccount(Borrower);
	allAccounts.addAccount(Owner1);
	allAccounts.addAccount(Owner2);

	// create three pieces of art all borrowed by mercy
	Artwork art1 = new Artwork("The Scream", "Edvard Munch", "borrowed", "mercy");
	Artwork art2 = new Artwork("Mona Lisa", "Leonardo da Vinci", "borrowed", "mercy");
	Artwork art3 = new Artwork("Starry Night", "Vincent van Gogh", "borrowed", "mercy");

	// set owner1 to own art1, art2 and owner 2 to own art3
	ArtList owner1Art = new ArtList();
	ArtList owner2ARt = new ArtList();
	owner1Art.add(art1);
	owner1Art.add(art2);
	owner2Art.add(art3);

	//connect myArt to owner account
	Owner1.setListing(owner1Art);
	Owner2.setListing(owner2Art);

	// put pieces of art in borrowers current Items list
	Borrower.currentItemsList.addItem(art1);
	Borrower.currentItemsList.addItem(art2);
	Borrower.currentItemsList.addItem(art3);

	// Create an intent and check that it equals the list created.
	Intent intent = new Intent();
	intent.putExtra(IntentReaderActivity.LIST_TO_VIEW_KEY, currentItemsList);
	setActivityIntent(intent);
	IntentReaderActivity ira = (IntentReaderActivity) getActivity();
	ListView listView = (Listview) ira.FindViewById(R.id.intentText);
	assertEquals("Incorrect View", currentItemsList, ira.getList());
    }
  
    // US 06.02.01
    // As an owner, I want to view a list of my things being borrowed, each thing with its description and borrower username.
    public void testViewBorrowedItems() {
	// Create two owners and one borrower
	UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");
	UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
    
	 //add to account list
  	 AccountList allAccounts = new AccountList();
   	 allAccounts.addAccount(Borrower);
   	 allAccounts.addAccount(Owner);
      
   	 // create three pieces of art all borrowed by mercy
	 Artwork art1 = new Artwork("The Scream", "Edvard Munch", "available", "");
   	 Artwork art2 = new Artwork("Mona Lisa", "Leonardo da Vinci", "borrowed", "mercy");
   	 Artwork art3 = new Artwork("Starry Night", "Vincent van Gogh", "borrowed", "mercy");
    
   	 // set owner1 to own art1, art2 and owner 2 to own art3
   	 ArtList myArt = new ArtList();
   	 myArt.add(art1);
   	 myArt.add(art2);
   	 myArt.add(art3);

	 //connect myArt to owner account
	 Owner.setListing(myArt);
	 assertTrue(Owner.getListing() != null);
      
   	 // put pieces of art in borrowers current Items list
	 Borrower.currentItemsList().addItem(art2);
	 Borrower.currentItemsList().addItem(art3);

	 // assert that desired list only contains art2 and art3
	 //expected list:
	 ArrayList<Artwork> expectedList = new ArrayList();
	 expectedList.add(art2);
	 expectedList.add(art3);

	 // Create an intent and check that it equals the list created.
    	 Intent intent = new Intent();
    	 intent.putExtra(IntentReaderActivity.LIST_TO_VIEW_KEY, borrowedItemsList);
    	 setActivityIntent(intent);
    	 IntentReaderActivity intentreaderactivity = (IntentReaderActivity) getActivity();
    	 ListView listView = (Listview) intentreaderactivity.FindViewById(R.id.intentText);
    	 assertEquals("Incorrect List", expectedList(), intentreaderactivity.getList());
    }
  
  
    // US 07.01.01
    // As an owner, I want to set a borrowed thing to be available when it is returned.
    public void testSetToAvailable() {
	//create new accounts
        UserAccount Borrower = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        UserAccount Owner = new UserAccount("pi314", "pi@hotmail.com", "Edmonton");

        //add to account list
        AccountList allAccounts = new AccountList();
        allAccounts.addAccount(Borrower);
        allAccounts.addAccount(Owner);

        //add art to owner account
        ArtList myArt = new ArtList();
        //Artwork attributes (title, artist, status, borrower)
        Artwork art = new Artwork("The Scream", "Edvard Munch", "borrowed", "mercy");
        myArt.add(art);

        //connect myArt to owner account
        Owner.setListing(myArt);

        // Add art to Borrower's borrowed list
        Borrower.currentItemsList.addItem(art);

        // check if added to listings and current items list
          
        // Owner sets status to 'available', 
     	Owner.getListing().getItem(0).setToAvailable();
      
        // assert that item was removed from borrower's current items list
     	// assert that myArt's status is 'available'
        assertEquals(Owner.getListing().getItem(0).getStatus(), "available");
        assertEquals(Borrower.getItemsList().getSize(), 0);
    }
  

    // US 08.01.01
    // As an owner, I want to define new things while offline, and push the additions once I get connectivity.
    public void testPushOfflineData() {
	UserAccount Owner = new UserAccount("mercy", "mercy@hotmail.com", "Edmonton");
        AccountList allAccounts = new AccountList();
        allAccounts.add(Owner);
        
        Artwork art = new Artwork("The Scream", "Edvard Munch", "available", "");
        ArtList myArt = new Artlist();
      
        //connect myArt to owner account
        Owner.setListing(myArt);
        assertTrue(Owner.getListing() != null);
      
        // set internet connection status to offline...
        // Not sure how to do this yet
        // add something
        myArt.add(art);
        
        // set internet connection status to online...
        // Not sure how to do this yet
        // push changes to profile once triggered by online status
        Owner.pushOfflineData();
      
        // assert that art has been added to artlist
        assertTrue(myArt.getSize(), 1);
    }
}
