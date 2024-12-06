/*
 * RUNI version of the Scrabble game.
 */
public class Scrabble {

	// Note 1: "Class variables", like the five class-level variables declared below,
	// are global variables that can be accessed by any function in the class. It is
	// customary to name class variables using capital letters and underline characters.
	// Note 2: If a variable is declared "final", it is treated as a constant value
	// which is initialized once and cannot be changed later.

	// Dictionary file for this Scrabble game
	static final String WORDS_FILE = "dictionary.txt";

	// The "Scrabble value" of each letter in the English alphabet.
	// 'a' is worth 1 point, 'b' is worth 3 points, ..., z is worth 10 points.
	static final int[] SCRABBLE_LETTER_VALUES = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3,
												  1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

	// Number of random letters dealt at each round of this Scrabble game
	static int HAND_SIZE = 10;

	// Maximum number of possible words in this Scrabble game
	static int MAX_NUMBER_OF_WORDS = 100000;

    // The dictionary array (will contain the words from the dictionary file)
	static String[] DICTIONARY = new String[MAX_NUMBER_OF_WORDS];

	// Actual number of words in the dictionary (set by the init function, below)
	static int NUM_OF_WORDS;

	// Populates the DICTIONARY array with the lowercase version of all the words read
	// from the WORDS_FILE, and sets NUM_OF_WORDS to the number of words read from the file.
	public static void init() {
		// Declares the variable in to refer to an object of type In, and initializes it to represent
		// the stream of characters coming from the given file. Used for reading words from the file.  
		In in = new In(WORDS_FILE);
        System.out.println("Loading word list from file...");
        NUM_OF_WORDS = 0;
		while (!in.isEmpty()) {
			// Reads the next "token" from the file. A token is defined as a string of 
			// non-whitespace characters. Whitespace is either space characters, or  
			// end-of-line characters.
			DICTIONARY[NUM_OF_WORDS++] = in.readString().toLowerCase();
		}
        System.out.println(NUM_OF_WORDS + " words loaded.");
	}

	// Checks if the given word is in the dictionary.
	public static boolean isWordInDictionary(String word) {
		for(int i = 0; i< DICTIONARY.length;i++){
			if(word.equals(DICTIONARY[i])) return true;
		}
			return false;
	}
	
	// Returns the Scrabble score of the given word.
	// If the length of the word equals the length of the hand, adds 50 points to the score.
	// If the word includes the sequence "runi", adds 1000 points to the game.
	public static int wordScore(String word) {
		int score = 0;
		int i = 0;
		int pos =0;
		for(i=0;i<word.length();i++){
			pos=(word.charAt(i)-97);
			score=SCRABBLE_LETTER_VALUES[pos]+score;
		}

        score= score*word.length();
		if(word.length()==10) score+=50;;
		if(word.contains("r")&&word.contains("u")&& word.contains("n")&&word.contains("i")) score+=1000;
		return score;
	}

	// Creates a random hand of length (HAND_SIZE - 2) and then inserts
	// into it, at random indexes, the letters 'a' and 'e'
	// (these two vowels make it easier for the user to construct words)
	public static String createHand() {
		String s =randomStringOfLetters(HAND_SIZE - 2);
		s=insertRandomly('a',s);
		s=insertRandomly('e',s);

		return s;
	}
	
    // Runs a single hand in a Scrabble game. Each time the user enters a valid word:
    // 1. The letters in the word are removed from the hand, which becomes smaller.
    // 2. The user gets the Scrabble points of the entered word.
    // 3. The user is prompted to enter another word, or '.' to end the hand. 
	public static void playHand(String hand) {
		int score = 0;
		In in = new In();
		while (hand.length() > 0) {
			System.out.println("Current Hand: " + spacedString(hand));
			System.out.println("Enter a word, or '.' to finish playing this hand:");
			String input = in.readString().trim();
			if (input.equals(".")) {
				break;
			}
			boolean checkContain = true;
			for (int i = 0; i < input.length(); i++) {
				if (!containsChar(hand, input.charAt(i))) {
					checkContain = false;
					break;
				}
			}
			if (!checkContain) {
				System.out.println("Invalid word. Try again.");
				continue;
			}	
			if (!isWordInDictionary(input)) {
				System.out.println("No such word in the dictionary. Try again.");
				continue;
			}
			if (!subsetOf(input, hand)) {
				System.out.println("Word is not a subset of the hand. Try again.");
				continue;
			}
			int wordScore = wordScore(input);
			score += wordScore;
			System.out.println(input + " earned " + wordScore + " points. Score: " + score + " points");
			hand = remove(hand, input);
		}
		System.out.println("End of hand. Total score: " + score + " points");
	}
	// Plays a Scrabble game. Prompts the user to enter 'n' for playing a new hand, or 'e'
	// to end the game. If the user enters any other input, writes an error message.
	public static void playGame() {
		// Initializes the dictionary
    	init();
		// The variable in is set to represent the stream of characters 
		// coming from the keyboard. Used for getting the user's inputs.  
		In in = new In();
		while(true) {
			System.out.println("Enter n to deal a new hand, or e to end the game:");
			// Gets the user's input, which is all the characters entered by 
			// the user until the user enter the ENTER character.
			String input = in.readString();
			if(input.equals("e")){
				break;
			}
			if(input.equals("n")){
				String s =createHand();
				playHand(s);
			}
			if(!((input.equals("n"))||(input.equals("e")))){
				System.out.println("error");
				continue;
			}
		}
	}

	public static void main(String[] args) {
		//// Uncomment the test you want to run
		testBuildingTheDictionary();  
		////testScrabbleScore();    
		////testCreateHands();  
		////testPlayHands();
		////playGame();
	}

	public static void testBuildingTheDictionary() {
		init();
		// Prints a few words
		for (int i = 0; i < 5; i++) {
			System.out.println(DICTIONARY[i]);
		}
		System.out.println(isWordInDictionary("mango"));
	}
	
	public static void testScrabbleScore() {
		System.out.println(wordScore("bee"));	
		System.out.println(wordScore("babe"));
		System.out.println(wordScore("friendship"));
		System.out.println(wordScore("running"));
	}
	
	public static void testCreateHands() {
		System.out.println(createHand());
		System.out.println(createHand());
		System.out.println(createHand());
	}
	public static void testPlayHands() {
		init();
		//playHand("ocostrza");
		//playHand("arbffip");
		//playHand("aretiin");
	}
	public static int countChar(String str, char ch) {
        int len = str.length();
        int i = 0;
        int c = 0;
        for(i = 0; i < len; i++){
            if(str.charAt(i)==ch) c++;
        }

        return c;
    }

    /** Returns true if str1 is a subset string str2, false otherwise
     *  Examples:
     *  subsetOf("sap","space") returns true
     *  subsetOf("spa","space") returns false
     *  subsetOf("pass","space") returns false
     *  subsetOf("c","space") returns true
     *
     * @param str1 - a string
     * @param str2 - a string
     * @return true is str1 is a subset of str2, false otherwise
     */
    public static boolean subsetOf(String str1, String str2) {
        for (int i = 0; i < str1.length(); i++) {
            char c = str1.charAt(i);
            int n = str2.indexOf(c);
            if (n == -1) {
                return false;
            }
            str2 = str2.substring(0, n) + str2.substring(n + 1);
        }

        return true;
    }



    /** Returns a string which is the same as the given string, with a space
     * character inserted after each character in the given string, except
     * for the last character. 
     * Example: spacedString("silent") returns "s i l e n t"
     * 
     * @param str - a string
     * @return a string consisting of the characters of str, separated by spaces.
     */
    public static String spacedString(String str) {
		if (str.isEmpty()) return str;
		String result = "" + str.charAt(0);
		for (int i = 1; i < str.length(); i++) {
			result = result + " " + str.charAt(i);
		}
		return result;
	}
  
    /**
     * Returns a string of n lowercase letters, selected randomly from 
     * the English alphabet 'a', 'b', 'c', ..., 'z'. Note that the same
     * letter can be selected more than once.
     * 
     * Example: randomStringOfLetters(3) can return "zoo"
     * 
     * @param n - the number of letter to select
     * @return a randomly generated string, consisting of 'n' lowercase letters
     */
    public static String randomStringOfLetters(int n) {
        int [] num = new int[n];
        String re = "";
        for(int i = 0;i<n;i++){
            num[i] = (int) (Math.random()* 26) + 97;
            re=re+(char)num[i];
        }

        return re;
    }

    /**
     * Returns a string consisting of the string str1, minus all the characters in the
     * string str2. Assumes (without checking) that str2 is a subset of str1.
     * Example: remove("meet","committee") returns "comit" 
     * 
     * @param str1 - a string
     * @param str2 - a string
     * @return a string consisting of str1 minus all the characters of str2
     */
	public static String remove(String str1, String str2) {
		char[] st1 = str1.toCharArray();
		char[] st2 = str2.toCharArray();
		String s = "";
	
		for (int i = 0; i < str2.length(); i++) {
			for (int b = 0; b < str1.length(); b++) {
				if (st1[b] == st2[i]) {
					st1[b] = (char) 0;
					break;
				}
			}
		}
	
		for (int b = 0; b < str1.length(); b++) {
			if (st1[b] != (char) 0) {
				s = s + st1[b];
			}
		}
	
		return s;
	}
    /**
     * Returns a string consisting of the given string, with the given 
     * character inserted randomly somewhere in the string.
     * For example, insertRandomly("s","cat") can return "scat", or "csat", or "cast", or "cats".  
     * @param ch - a character
     * @param str - a string
     * @return a string consisting of str with ch inserted somewhere
     */
    public static String insertRandomly(char ch, String str) {
         // Generate a random index between 0 and str.length()
         int randomIndex = (int) (Math.random() * (str.length() + 1));
         // Insert the character at the random index
         String result = str.substring(0, randomIndex) + ch + str.substring(randomIndex);
         return result;
    }    
	public static boolean containsChar(String str, char c) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				return true;
			}
		}
		return false;
}
