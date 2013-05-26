/**
 * 
 * 	���ڻ���Բ
 */
package com.bn.st.xc;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import com.bn.core.MatrixState;

import android.opengl.GLES20;
//��triangle_fan��ʽ����Բ�� ��Բ����ƽ����XYƽ���
public class CircleForDraw
{
	int mProgram;//�Զ�����Ⱦ����ɫ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������id
    int maPositionHandle; //����λ����������id  
    int maNormalHandle; //���㷨������������id
    int muMMatrixHandle;//λ�á���ת�任����
    int maLightLocationHandle;//��Դλ����������id  
    int maCameraHandle; //�����λ����������id   
        
    int maColorR;	//��ɫֵ��R��������id
    int maColorG;	//��ɫֵ��G��������id
    int maColorB;	//��ɫֵ��B��������id
    int maColorA;
    
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	private static FloatBuffer   mVertexBuffer;//�����������ݻ���
    private static FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
    private static int vCount;
 
    float r;	//��ɫֵ��R����
    float g;	//��ɫֵ��G����
    float b;	//��ɫֵ��B����
    public CircleForDraw
    (
            int mProgram,
    		float angleSpan,//�зֽǶ�
    		float radius,//Բ�뾶
    		float[]color
    )
    {
    	this.r=color[0];
    	this.g=color[1];
    	this.b=color[2];
    	initShader(mProgram);
    }
    public static void initVertexData
    (
    		float angleSpan,//�зֽǶ�
    		float radius//Բ�뾶
    )
    {
    	//���������������ݵĳ�ʼ��================begin============================
    	vCount=1+(int)(360/angleSpan)+1;//����ĸ���
    	
    	float[] vertices=new float[vCount*3];//��ʼ����������
    	float[] normals=new float[vCount*3];//��ʼ�����㷨��������
    	
    	//������ĵ�����
    	vertices[0]=0;
    	vertices[1]=0;
    	vertices[2]=0;
    	
    	//������ĵ㷨����
    	normals[0]=0;
        normals[1]=0;
        normals[2]=1;
        
    	int vcount=3;//��ǰ������������
    	int ncount=3;//��ǰ��������������
    	
    	for(float angle=0;angle<=360;angle=angle+angleSpan)
    	{
    		double angleRadian=Math.toRadians(angle);
    		//��������
    		vertices[vcount++]=radius*(float)Math.cos(angleRadian);
    		vertices[vcount++]=radius*(float)Math.sin(angleRadian);
    		vertices[vcount++]=0;
    		normals[ncount++]=0;
    		normals[ncount++]=0;
    		normals[ncount++]=1;
    	}  
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��

                
        //�������㷨�������ݻ���
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥����������
        mNormalBuffer.position(0);//���û�������ʼλ��
    }
    public void initShader(int mProgramIn)
    {
        mProgram = mProgramIn;
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������id  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ�����й�Դλ������id
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����������λ������id
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        
        maColorR=GLES20.glGetUniformLocation(mProgram, "colorR");
        maColorG=GLES20.glGetUniformLocation(mProgram, "colorG");
        maColorB=GLES20.glGetUniformLocation(mProgram, "colorB");
        maColorA=GLES20.glGetUniformLocation(mProgram, "colorA");
    }
    public void drawSelf(float alpha)
    {        
    	//�ƶ�ʹ��ĳ��shader����
   	 	GLES20.glUseProgram(mProgram); 
        //�����ձ任������shader����
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        //��λ�á���ת�任������shader����
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);  
        //����Դλ�ô���shader����   
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        //�������λ�ô���shader����   
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        //���붥��λ������	
        GLES20.glVertexAttribPointer  
        (
        		maPositionHandle,   
        		3, 
        		GLES20.GL_FLOAT, 
        		false,
               3*4,   
               mVertexBuffer
        );  
        //���붥�㷨��������
        GLES20.glVertexAttribPointer  
        (
        		maNormalHandle, 
        		3,   
        		GLES20.GL_FLOAT, 
        		false,
               3*4,   
               mNormalBuffer
        );  
        //��������λ�á���������������
        GLES20.glEnableVertexAttribArray(maPositionHandle);  
        GLES20.glEnableVertexAttribArray(maNormalHandle);  
        GLES20.glUniform1f(maColorR , r); 
        GLES20.glUniform1f(maColorG , g); 
        GLES20.glUniform1f(maColorB , b);
        GLES20.glUniform1f(maColorA , alpha);
        //����ͼ��
        GLES20.glDrawArrays
        (
        		GL10.GL_TRIANGLE_FAN, 		//��TRIANGLE_FAN��ʽ���
        		0,
        		vCount
        );
    }
}