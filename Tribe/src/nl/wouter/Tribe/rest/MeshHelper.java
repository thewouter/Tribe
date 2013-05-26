package nl.wouter.Tribe.rest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class MeshHelper {
	private Mesh mesh;
	private ShaderProgram meshShader;
	
	public MeshHelper(){
		createShader();
	}
	
	public void createMesh(float[] vertices){
		mesh = new Mesh(true, vertices.length, 0, new VertexAttribute(Usage.Position, 2, "a_position"));
		mesh.setVertices(vertices);
	}
	
	private void createShader(){
		String vertexShader = "attribute vec4 a_position;    \n"
							+ "void main()                   \n"
							+ "{                             \n"
							+ "   gl_Position = a_position;  \n"
							+ "}                             \n";
		String fragmentShader = "#ifdef GL_ES                \n"
                			+ "precision mediump float;    \n"
                			+ "#endif                      \n"
                			+ "void main()                 \n"
                			+ "{                           \n"
                			+ "  gl_FragColor = vec4(1.0,0.0,0.0,1.0);    \n"
                			+ "}";
		meshShader = new ShaderProgram(vertexShader, fragmentShader);
		if(!meshShader.isCompiled()){
			throw new IllegalStateException(meshShader.getLog());
			
		}
	}
	
	public void translateInPixels(int x ,int y){
		translate((float) x / ((float)Gdx.graphics.getWidth() / 4), (float) y / ((float)Gdx.graphics.getHeight() / 4));
	}
	
	private void translate(float x, float y){
		Matrix4 matrix = new Matrix4();
		matrix.translate(x, -y, 0);
		mesh.transform(matrix);
	}
	
	public void drawMesh(SpriteBatch batch){
		batch.end();
		if(mesh == null){
			throw new IllegalStateException("drawing mesh before mesh has been created");
		}
		meshShader.begin();
		mesh.render(meshShader, GL20.GL_LINE_LOOP);
		meshShader.end();
		batch.begin();
	}
	
	public void dispose(){
		mesh.dispose();
		meshShader.dispose();
	}

}
