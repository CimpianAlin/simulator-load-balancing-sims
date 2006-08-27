package messages;

public class ChkDataFound extends Message
{
	public ChkDataFound (int id)
	{
		this.id = id;
		size = Message.HEADER_SIZE;
	}
	
	public String toString()
	{
		return new String ("CHK data found (" + id + ")");
	}
}
