	package main;
	
	import javafx.application.Application;
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.geometry.Pos;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.ComboBox;
	import javafx.scene.control.Label;
	import javafx.scene.image.Image;
	import javafx.scene.image.ImageView;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.layout.HBox;
	import javafx.scene.layout.VBox;
	import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
	import java.io.File;
	import javax.imageio.ImageIO;
	
	public class Main extends Application {
	    private File selectedFile;
	
	    @Override
	    public void start(Stage primaryStage) {
	        // Halaman upload
	        Label titleLabel = new Label("Upload and Transform Your Image");
	        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-padding: 10px;");

	        Label uploadLabel = new Label("Choose an image:");
	        uploadLabel.setStyle("-fx-font-size: 18px;");

	        Label selectedFileLabel = new Label("No file selected");
	        selectedFileLabel.setStyle("-fx-font-size: 16px;");

	        Label optionLabel = new Label("Choose an option:");
	        optionLabel.setStyle("-fx-font-size: 18px;");

	        Button browseButton = new Button("Browse...");
	        browseButton.setPrefWidth(200);
	        browseButton.setStyle("-fx-font-size: 16px;");

	        Button uploadButton = new Button("Upload and Convert");
	        uploadButton.setPrefWidth(200);
	        uploadButton.setStyle("-fx-font-size: 16px;");
	        uploadButton.setDisable(true);

	        ComboBox<String> optionComboBox = new ComboBox<>(FXCollections.observableArrayList("Grayscale", "Blur"));
	        optionComboBox.setValue("Grayscale");
	        optionComboBox.setPrefWidth(200);
	        optionComboBox.setStyle("-fx-font-size: 16px;");

	        browseButton.setOnAction(e -> {
	            FileChooser fileChooser = new FileChooser();
	            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
	            selectedFile = fileChooser.showOpenDialog(primaryStage);
	            if (selectedFile != null) {
	                selectedFileLabel.setText("Selected file: " + selectedFile.getName());
	                uploadButton.setDisable(false);
	            }
	        });

	        uploadButton.setOnAction(e -> {
	            if (selectedFile != null) {
	                String selectedOption = optionComboBox.getValue();
	                showImageEditor(primaryStage, selectedOption);
	            }
	        });

	        VBox contentBox = new VBox(20, uploadLabel, selectedFileLabel, browseButton, optionLabel, optionComboBox, uploadButton);
	        contentBox.setStyle("-fx-background-color: white; -fx-padding: 40px; -fx-spacing: 20px; -fx-border-color: #ccc; -fx-border-width: 2px;");
	        contentBox.setAlignment(Pos.CENTER);

	        BorderPane rootPane = new BorderPane();
	        rootPane.setTop(titleLabel);
	        rootPane.setCenter(contentBox);
	        rootPane.setStyle("-fx-background-color: #D3D3D3; -fx-padding: 50px;");
	        BorderPane.setAlignment(titleLabel, Pos.CENTER);

	        Scene uploadScene = new Scene(rootPane, 800, 600);

	        primaryStage.setTitle("Image Converter");
	        primaryStage.setScene(uploadScene);

	        Screen screen = Screen.getPrimary();
	        javafx.geometry.Rectangle2D bounds = screen.getBounds();
	        primaryStage.setX(bounds.getMinX());
	        primaryStage.setY(bounds.getMinY());
	        primaryStage.setWidth(bounds.getWidth());
	        primaryStage.setHeight(bounds.getHeight() - 50);

	        primaryStage.show();
	    }

	    private void showImageEditor(Stage primaryStage, String selectedOption) {
	    	//halaman hasil
	        try {
	            BufferedImage originalImage = ImageIO.read(selectedFile);
	            BufferedImage transformedImage = selectedOption.equals("Grayscale") ? toGrayscale(originalImage) : toBlur(originalImage);

	            Image originalFxImage = convertToFxImage(originalImage);
	            Image transformedFxImage = convertToFxImage(transformedImage);

	            ImageView originalView = new ImageView(originalFxImage);
	            originalView.setFitWidth(500);
	            originalView.setPreserveRatio(true);

	            ImageView transformedView = new ImageView(transformedFxImage);
	            transformedView.setFitWidth(500);
	            transformedView.setPreserveRatio(true);

	            Label titleLabel = new Label("Original and Transformed Image");
	            titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-padding: 10px;");

	            Label originalLabel = new Label("Original Image");
	            originalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

	            Label transformedLabel = new Label(selectedOption + " Image");
	            transformedLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

	            VBox originalBox = new VBox(10, originalLabel, originalView);
	            originalBox.setAlignment(Pos.CENTER);

	            VBox transformedBox = new VBox(10, transformedLabel, transformedView);
	            transformedBox.setAlignment(Pos.CENTER);

	            HBox imageBox = new HBox(30, originalBox, transformedBox);
	            imageBox.setAlignment(Pos.CENTER);

	            Button backButton = new Button("Back");
	            backButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
	            backButton.setOnAction(e -> start(primaryStage));

	            VBox root = new VBox(30, titleLabel, imageBox, backButton);
	            root.setAlignment(Pos.CENTER);
	            root.setStyle("-fx-background-color: white; -fx-padding: 40px; -fx-spacing: 30px; -fx-border-color: #ccc; -fx-border-width: 2px;");

	            BorderPane bp = new BorderPane();
	            bp.setTop(titleLabel);
	            bp.setCenter(root);
	            bp.setStyle("-fx-background-color: #D3D3D3; -fx-padding: 50px;");
		        BorderPane.setAlignment(titleLabel, Pos.CENTER);

	            Scene editorScene = new Scene(bp, 800, 600);
	            primaryStage.setScene(editorScene);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }	
	
	    private BufferedImage toGrayscale(BufferedImage img) {
	        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        for (int y = 0; y < img.getHeight(); y++) {
	            for (int x = 0; x < img.getWidth(); x++) {
	                int rgb = img.getRGB(x, y);
	                Color color = new Color(rgb, true);
	                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
	                result.setRGB(x, y, new Color(gray, gray, gray).getRGB());
	            }
	        }
	        return result;
	    }
	
	    private BufferedImage toBlur(BufferedImage img) {
	        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        for (int y = 1; y < img.getHeight() - 1; y++) {
	            for (int x = 1; x < img.getWidth() - 1; x++) {
	                int avgR = 0, avgG = 0, avgB = 0;
	                for (int ky = -1; ky <= 1; ky++) {
	                    for (int kx = -1; kx <= 1; kx++) {
	                        int rgb = img.getRGB(x + kx, y + ky);
	                        Color color = new Color(rgb, true);
	                        avgR += color.getRed();
	                        avgG += color.getGreen();
	                        avgB += color.getBlue();
	                    }
	                }
	                avgR /= 9;
	                avgG /= 9;
	                avgB /= 9;
	                result.setRGB(x, y, new Color(avgR, avgG, avgB).getRGB());
	            }
	        }
	        return result;
	    }
	
	    private Image convertToFxImage(BufferedImage img) {
	        return javafx.embed.swing.SwingFXUtils.toFXImage(img, null);
	    }
	
	    public static void main(String[] args) {
	        launch(args);
	    }
	}