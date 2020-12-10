package application;

public class Words extends Main {

	private int wordID;
	private String wordText;
	private int occurence;
	
	public Words(int wordId, String wordText, int occurence) {
		this.wordID = wordId;
		this.wordText = wordText;
		this.occurence = occurence;
	}

	public int getWordID() {
		return wordID;
	}

	public void setWordID(int wordID) {
		this.wordID = wordID;
	}

	public String getWordText() {
		return wordText;
	}

	public void setWordText(String wordText) {
		this.wordText = wordText;
	}

	public int getOccurence() {
		return occurence;
	}

	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}
	
	
}
