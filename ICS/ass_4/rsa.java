import java.math.BigInteger;
import java.util.Scanner;

public class rsa {
	
	public static boolean isPrime(long num) {
		if(num==1||num==2)
			return true;
		for(int i=2;i<Math.sqrt(num);i++) {
			if(num%i==0)
				return false;
		}
		return true;
	}

	static long gcd(long phiOfN, long e) { 
		long temp; 
	    while (true) 
	    { 
	        temp = phiOfN%e; 
	        if (temp == 0) 
	          return e; 
	        phiOfN = e; 
	        e = temp; 
	    }
	}
	
	public static long getEncyptionkey(long phiOfN) {
		long e=2;
		for(e=2;e<phiOfN;e++) {
			if(gcd(phiOfN,e)==1) {
				//e=13;
				return e;
			}
		}
		//e=13;
		
		return e;
	}
	
	public static long getDecryptionKey(long phiOfN, long e) {
		double num=0.0;
		for(int i=1;i<100;i++) {
			num=(double)(i*phiOfN+1)/e;  
			if(Math.floor(num)==num) 
			{
				//System.out.println("i = "+i+" num = "+num);
				break;
			}
		}
		return (long)num;
	}
	
	public static void main(String[] args)
	{
		Scanner sc=new Scanner(System.in);
		
		System.out.print("\n********** RSA ***********");
		System.out.print("\n----- Encryption:-------------\n");
		System.out.print("Enter Prime number (P):");
		long p=sc.nextInt();
		System.out.print("Enter Prime number (Q):");
		long q=sc.nextInt();
		
		long n=p*q;
		
		long phiOfN=(p-1)*(q-1);
		
		int e=(int)getEncyptionkey(phiOfN);
		
		System.out.println("P = "+p+"\nQ = "+q);
		System.out.println("N = "+n);
		System.out.println("Phi(n) = "+phiOfN);
		System.out.println("Public key (e) = "+e);
		//System.out.println("Private Key (d) = "+d);
		
		System.out.println("Enter message(number) to be encrypted");
		long PT=sc.nextInt();
		 System.out.println("Plain Text : "+PT);
		 BigInteger CT=(BigInteger.valueOf(PT).pow(e)).mod(BigInteger.valueOf(n));	
		 	
		System.out.println("Cipher Text: "+CT);
		
		
		System.out.print("\n----- Decryption:-------------\n");
		
		
		
		long CT1 = CT.longValue();
		
		System.out.println("N = "+n);
		System.out.println("Public key (e) = "+e);
		System.out.println("phi(n) = "+phiOfN);
		System.out.println("Cipher text : "+CT1);
		
		long d=getDecryptionKey(phiOfN, e);
		System.out.println("Private key (D):  "+d);
		BigInteger PT1=(BigInteger.valueOf(CT1).pow((int)d)).mod(BigInteger.valueOf(n));
			
		
		System.out.println("Decypted Plain text: "+PT1);
				
	}
}//class end
