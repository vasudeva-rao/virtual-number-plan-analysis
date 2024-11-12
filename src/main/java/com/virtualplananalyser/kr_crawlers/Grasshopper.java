package com.virtualplananalyser.kr_crawlers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class Grasshopper {

    /**
     * This method is used to crawl the grasshopper website and save the crawled data into csv files
     */
    public static void crawl_grasshopper() {
        // Setting up chrome driver through WebDriverManager.
        WebDriverManager.chromedriver().setup();

        // Creating an instance of the Chrome driver
        WebDriver kr_driver = new ChromeDriver();

        // Creating an instance of the WebDriverWait
        WebDriverWait kr_wait = new WebDriverWait(kr_driver, Duration.ofSeconds(10));

        // Try catch block to handle the exceptions
        try {
            // Visiting the Grasshopper website
            kr_driver.get("https://grasshopper.com/");

            // Get the title of the website and verify it with Assertions
            String kr_title = kr_driver.getTitle();
            Assertions.assertEquals(kr_title, "Grasshopper Virtual Phone System | Start Your Free Trial");

            // Get the url of the website and verify it with Assertions
            String kr_url = kr_driver.getCurrentUrl();
            Assertions.assertEquals(kr_url, "https://grasshopper.com/");

            // Getting the cookie accept button element and accepting the cookies using Explicit Waiting technique
            WebElement kr_acceptConsentBtn = kr_wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("truste-consent-button")));
            kr_acceptConsentBtn.click();

            // Closing the Open Chat Support window which is in different IFrame
            WebElement kr_chat_iframe_element = kr_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"web-messenger-container\"]")));
            // Switching to the IFrame of the chat window
            kr_driver.switchTo().frame(kr_chat_iframe_element);
            // Getting Close button
            WebElement kr_close_chat_btn = kr_wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".FirstMessageChat__closeButtonWrapper--xoodk.khCloseButtonWrapper")));
            // Closing the chat support
            kr_close_chat_btn.click();
            // Switching back to the default IFrame
            kr_driver.switchTo().defaultContent();

            // Getting the Buy Now Button
            WebElement kr_buyNowButton = kr_wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Buy Now")));
            // Creating the instance of the Actions class
            Actions kr_actions = new Actions(kr_driver);
            // Scrolling to the Buy Now button element
            kr_actions.moveToElement(kr_buyNowButton);
            // Clicking on the Buy Now button
            kr_buyNowButton.click();

            // Adding an implicit wait of 10 seconds
            kr_driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // Getting data to add to CSV file
            // Getting plan cards
            List<WebElement> kr_plan_cards = kr_driver.findElements(By.className("plan-card__wrapperh_J"));
            // Creating CSV file within try catch to handle any IO Exceptions
            try (PrintWriter kr_writer = new PrintWriter(new FileWriter("kr_scraped_data.csv"))) {
                kr_writer.write("Plan Name,Plan Description,Amount,Users,Phone Numbers,Extensions\n");
                for (WebElement kr_plan_card : kr_plan_cards) {
                    // Getting card text
                    String kr_card_ext = kr_plan_card.getText();
                    // Splitting the text based on new line character
                    String[] kr_lines = kr_card_ext.split("\n");
                    // Removing the special character
                    for (int i = 0; i < kr_lines.length; i++) {
                        kr_lines[i] = kr_lines[i].replaceAll("ⓘ", "").trim();
                    }
                    // Extracting data
                    String kr_plan_name = kr_lines[1].trim();
                    String kr_plan_description = kr_lines[2].trim();
                    String kr_amount = "$" + kr_lines[6].trim() + " " + kr_lines[7].trim();
                    String kr_users = kr_lines[10].trim();
                    String kr_phone_numbers = kr_lines[11].trim();
                    String kr_extensions = kr_lines[12].trim();
                    // Writing data into the CSV file
                    kr_writer.write(kr_plan_name + "," + kr_plan_description + "," + kr_amount + "," + kr_users + "," + kr_phone_numbers + "," + kr_extensions + "\n");
                }
                System.out.println("Data Extracted to the CSV File - Plan Types!!!");
            } catch (IOException e) {
                System.out.println("Exception occurred!!!!!" + e.getMessage());
            }

            // Scraping multiple pages
            WebElement kr_buy_true_solo = kr_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/main/div[1]/div[2]/div[2]/div[1]/div/div[2]/a")));
            kr_buy_true_solo.click();

            // Adding an implicit wait of 10 seconds
            kr_driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // Verify the url is changed
            kr_url = kr_driver.getCurrentUrl();
            Assertions.assertEquals(kr_url, "https://signup.grasshopper.com/numbers");
            // Scraping the type of numbers page
            // Getting data to add to CSV file
            // Getting types of number
            List<WebElement> kr_number_types = kr_driver.findElements(By.className("contentBox__linkbox39S"));
            // Creating CSV file within try catch to handle any IO Exceptions
            try (PrintWriter kr_writer = new PrintWriter(new FileWriter("kr_scraped_number_types_data.csv"))) {
                kr_writer.write("Number Type, Description\n");
                for (WebElement kr_number_type : kr_number_types) {
                    // Getting card text
                    String kr_card_text = kr_number_type.getText();
                    // Splitting the text based on new line character
                    String[] kr_lines = kr_card_text.split("\n");
                    // Extracting data
                    String kr_number_type_name = kr_lines[0].trim();
                    String kr_number_type_description = kr_lines[1].trim();
                    // Writing data into the CSV file
                    kr_writer.write(kr_number_type_name + "," + kr_number_type_description.replaceAll(",", ";").trim() + "\n");
                }
                System.out.println("Data Extracted to the CSV File - Number types!!!");
            } catch (IOException e) {
                System.out.println("Exception occurred!!!!!" + e.getMessage());
            }

            // Combining the two csv files
            String kr_file1 = "kr_scraped_data.csv";
            String kr_file2 = "kr_scraped_number_types_data.csv";
            String kr_output_file = "kr_combined_data.csv";
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(kr_output_file))) {
                // Combine both CSV files
                kr_combine_csv_files(kr_file1, writer);
                kr_combine_csv_files(kr_file2, writer);
                System.out.println("CSV files combined successfully!!!");
            } catch (IOException e) {
                System.out.println("Exception occurred!!!!!" + e.getMessage());
            }
        } finally {
            // Close the browser
            kr_driver.quit();
        }
    }

    /**
     * This method is used to combine the CSV files
     *
     * @param kr_file_path path to the file
     * @param kr_writer    BufferedWriter
     **/
    public static void kr_combine_csv_files(String kr_file_path, BufferedWriter kr_writer) {
        try (BufferedReader kr_reader = Files.newBufferedReader(Paths.get(kr_file_path))) {
            String kr_line;
            while ((kr_line = kr_reader.readLine()) != null) {
                kr_writer.write(kr_line);
                // Write each line to the new file
                kr_writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Exception occurred!!!!!" + e.getMessage());
        }
    }
}
