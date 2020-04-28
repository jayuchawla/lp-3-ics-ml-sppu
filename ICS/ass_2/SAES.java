import java.util.Scanner;


public class SAES {
	Scanner sc=new Scanner(System.in);
	int key[]={0,1,0,0,  1,0,1,0,   1,1,1,1,  0,1,0,1};
	
	int pltext[]={1,1,0,1,  0,1,1,1,   0,0,1,0,  1,0,0,0};
	int num1[]={1,0,0,0,0,0,0,0};
	int num2[]={0,0,1,1,0,0,0,0};
	
	int sencry[][]={{9,4,10,11},{13,1,8,5},{6,2,0,3},{12,14,15,7}};
	int sdecrp[][]={{10,4,9,11},{1,7,8,15},{6,0,2,3},{12,4,13,14}};
	int w0[]=new int[8];
	int w1[]=new int[8];
	int w2[]=new int[8];
	int w3[]=new int[8];
	int w4[]=new int[8];
	int w5[]=new int[8];
	int k0[]=new int[16];
	int k1[]=new int[16];
	int k2[]=new int[16];
	int mixmat[]={1,4,4,1};
	int imixmat[]={9,2,2,9};
	private int xorresults[];
	private int[] add1;
	
	public int bitToInt(int a,int b)
	{
		if(a==0 && b==0)
			return 0;
		else if(a==0 && b==1)
			return 1;
		else if(a==1 && b==0)
			return 2;
		return 3;
	}
	public int bToInt(int a[],int start,int size)
	{
		int result=0;
		
		for(int i=size-1,k=start;i>=0;i--,k++)
		{
			result+=a[k]*Math.pow(2, i);
		}
		return result;
	}
	public int[] intToBin(int y)
	{
		int result[]=new int[4];
		
		result[3]=y%2;
		y=y/2;
		result[2]=y%2;
		y=y/2;
		result[1]=y%2;
		y=y/2;
		result[0]=y%2;
		
		return result;
		
	}
	
	public void display_array(int a[])
	{
		for(int i=0;i<a.length;i++)
			System.out.print(a[i]);
		System.out.print("\n");
	}
	public void accept()
	{
		System.out.print("enter 16 bit key");
		for(int i=0;i<16;i++)
		key[i]=sc.nextInt();		
		
		
		System.out.print("enter 16 bit text");
		for(int i=0;i<16;i++)
		pltext[i]=sc.nextInt();
		
		display_array(pltext);
	}
	
	public int[] xor(int a[],int b[])
	{
		int y[]=new int[a.length];
		
		for(int i=0;i<a.length;i++)
			 if(a[i]==b[i])
				 y[i]=0;
			 else
				  y[i]=1;
		return y;
	}
	
	public  int[] rotateNibble(int a[])
	{
		int temp[]=new int[8];
		System.arraycopy(a, 0,temp , 4, 4);
		System.arraycopy(a, 4,temp , 0, 4);
		return temp;
	}
	public  int[] subNibble(int a[],int s[][])
	{
		if(a.length==8)
		{
		int temp[]=new int[8];
		
		int L[]=new int[4];
		int R[]=new int[4];
		
		int row1=bitToInt(a[0],a[1]);
		int row2=bitToInt(a[4],a[5]);
		int col1=bitToInt(a[2],a[3]);
		int col2=bitToInt(a[6],a[7]);
		
		int x=s[row1][col1];
		int y=s[row2][col2];

		L=intToBin(x);
		R=intToBin(y);
		
		System.arraycopy(L, 0,temp , 0, 4);
		System.arraycopy(R, 0,temp , 4, 4);
		
		return temp;
		}
		else if(a.length==16)
		{
		int temp[]=new int[16];
		
		int L1[]=new int[4];
		int R1[]=new int[4];
		int L2[]=new int[4];
		int R2[]=new int[4];
		
		int row1=bitToInt(a[0],a[1]);
		int row2=bitToInt(a[4],a[5]);
		int row3=bitToInt(a[8],a[9]);
		int row4=bitToInt(a[12],a[13]);
		
		int col1=bitToInt(a[2],a[3]);
		int col2=bitToInt(a[6],a[7]);
		int col3=bitToInt(a[10],a[11]);
		int col4=bitToInt(a[14],a[15]);
		
		int x1=s[row1][col1];
		int y1=s[row2][col2];
		int x2=s[row3][col3];
		int y2=s[row4][col4];

		L1=intToBin(x1);
		L2=intToBin(y1);
		R1=intToBin(x2);
		R2=intToBin(y2);
		
		System.arraycopy(L1, 0,temp , 0, 4);
		System.arraycopy(L2, 0,temp , 4, 4);
		System.arraycopy(R1, 0,temp , 8, 4);
		System.arraycopy(R2, 0,temp , 12, 4);
		
		return temp;
		}
		return a;
	}
	
	public int[] shift_row(int a[])
	{
		int temp[]=new int[4];
		System.arraycopy(a, 4,temp , 0, 4);
		
		System.arraycopy(a, 12,a, 4, 4);
		System.arraycopy(temp, 0,a, 12, 4);
		
		return a;
		
	}
	public int mixmul(int a,int  b)
	{int c=0;
		for (int bit=3;bit>=0;bit--)
		{int x=b &(1<<bit);
		 if (x!=0)
			c=c^(a<<bit);
		}

	// Reduce back into galois field (can also be folded into above loop)
	int irr=0x13; // represents reduction polynomial
	int high=0x10; // high bit of above
	for (int bit=3;bit>=0;bit--) {
		int x=c&(high<<bit);
		if (x!=0)  // if that bit is set
			c=c^(irr<<bit); // galois "subtract" it off
	}
	return c;
	}
	
	public void keygeneration()
	{
		System.arraycopy(key, 0,w0 , 0, 8);
		System.arraycopy(key, 8,w1 , 0, 8);
		
		System.out.print("w0=");
		display_array(w0);
		System.out.print("w1=");
		display_array(w1);
		
		w2=xor(w0,num1);
		
		w2=xor(w2,subNibble(rotateNibble(w1),sencry));
		
		
		System.out.print("w2=");
		display_array(w2);
		
		w3=xor(w2,w1);
		System.out.print("w3=");
		display_array(w3);
		
		System.out.print("w3=");
		display_array(w3);
		
		w4=xor(w2,num2);
		w4=xor(w4,subNibble(rotateNibble(w3),sencry));
		
		System.out.print("w4=");
		display_array(w4);
		
		w5=xor(w4,w3);
		System.out.print("w5=");
		display_array(w5);
		
		System.arraycopy(w0, 0,k0 , 0, 8);
		System.arraycopy(w1, 0,k0 , 8, 8);
		System.out.print("k0=");
		display_array(k0);
		
		System.arraycopy(w2, 0,k1 , 0, 8);
		System.arraycopy(w3, 0,k1 , 8, 8);
		System.out.print("k1=");
		display_array(k1);
		
		
		System.arraycopy(w4, 0,k2, 0, 8);
		System.arraycopy(w5, 0,k2 , 8, 8);
		System.out.print("k2=");
		display_array(k2);
		
		 add1 = xor(pltext,k0);
		System.out.print("add round key=");
		display_array(add1);
		
		
	}
	public int[] encryption()
	{
		//================================================ROUND1==========================================================
				int subnib1[]=subNibble(add1,sencry);
				System.out.print("sub nib1=");
				display_array(subnib1);
				
				add1=shift_row(subnib1);
				
				System.out.print("shift rows=");
				display_array(add1);
			  
				int b[]=new int[4];
				b[0]=bToInt(add1,0,4);
				b[2]=bToInt(add1,4,4);
				b[1]=bToInt(add1,8,4);
				b[3]=bToInt(add1,12,4);
				
				int s[]=new int[16];
				System.out.println(b[0]+" "+b[1]+" "+b[2]+" "+b[3]);
				 s[0]= mixmul(mixmat[0],b[0]) ^ mixmul(mixmat[1],b[2]) ;
				 s[1]= mixmul(mixmat[0],b[1]) ^ mixmul(mixmat[1],b[3]) ;
				 s[2]= mixmul(mixmat[2],b[0]) ^ mixmul(mixmat[3],b[2]) ;
				 s[3]= mixmul(mixmat[2],b[1]) ^ mixmul(mixmat[3],b[3]) ;
				
				 //System.out.println(s[0]+" "+s[1]+" "+s[2]+" "+s[3]);
				
				 int temp[]=intToBin(s[3]);
				 System.arraycopy(temp, 0,s , 12, 4);
				 temp=intToBin(s[2]);
				 System.arraycopy(temp, 0,s , 4, 4);
				 temp=intToBin(s[1]);
				 System.arraycopy(temp, 0,s , 8, 4);
				 temp=intToBin(s[0]);
				 System.arraycopy(temp, 0,s , 0, 4);

				 System.out.print("S=");
				 display_array(s);
				 
				 s=xor(s,k1);

				 System.out.print("k1");
				 display_array(k1);
				 
				 System.out.print("Round 1 op");
				 display_array(s);
				 
				 //=====================================ROUND2==========================================================
				s= subNibble(s,sencry);
				System.out.print("\n sub nib");
				 display_array(s);
				 
				 s= shift_row(s);
					 System.out.print("shift row");
					 display_array(s);
				
				s=xor(s,k2);
				 System.out.print("\n round 2");
				 display_array(s);
		return s;
	}
	private void decryption(int[] cyphertext) {
		int s[]=xor(cyphertext,k2);
		 s=shift_row(s);
		
		 System.out.print("Inverse shift  :");
		 display_array(s);
		 
		 s=subNibble(s,sdecrp);
		 
		 s=xor(k1,s);
		 
		 int b[]=new int[4];
			b[0]=bToInt(s,0,4);
			b[2]=bToInt(s,4,4);
			b[1]=bToInt(s,8,4);
			b[3]=bToInt(s,12,4);
			
			System.out.println(b[0]+" "+b[1]+" "+b[2]+" "+b[3]);
			 s[0]= mixmul(imixmat[0],b[0]) ^ mixmul(imixmat[1],b[2]) ;
			 s[1]= mixmul(imixmat[0],b[1]) ^ mixmul(imixmat[1],b[3]) ;
			 s[2]= mixmul(imixmat[2],b[0]) ^ mixmul(imixmat[3],b[2]) ;
			 s[3]= mixmul(imixmat[2],b[1]) ^ mixmul(imixmat[3],b[3]) ;
			 
			 int temp[]=intToBin(s[3]);
			 System.arraycopy(temp, 0,s , 12, 4);
			 temp=intToBin(s[2]);
			 System.arraycopy(temp, 0,s , 4, 4);
			 temp=intToBin(s[1]);
			 System.arraycopy(temp, 0,s , 8, 4);
			 temp=intToBin(s[0]);
			 System.arraycopy(temp, 0,s , 0, 4);

			 System.out.print("S=");
			 display_array(s);
			 
			 s=shift_row(s);
			 System.out.print("Inverse shift =");
			 display_array(s); 
			
			 s=subNibble(s,sdecrp);
			 System.out.print("Inverse Nibble =");
			 display_array(s); 
			 
			 s=xor(k0,s);
			 System.out.print("Plain Text=");
			 display_array(s); 
		 
	}
	public static void main(String args[])
	{
		SAES obj=new SAES();	
		//obj.accept();
		obj.keygeneration();
		int cyphertext[]=obj.encryption();
		
		 System.out.print("==============================Decryption=========================================\n");
		obj.decryption(cyphertext);
		
		
	}
	

}
