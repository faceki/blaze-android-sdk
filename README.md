<h1 align="center">
    Native Android SDK For FACEKI BLAZE 3.0
</h1>

<p align="center">
  <strong>Faceki V2 Know Your Customer</strong><br>
</p>

## Client Id and Client Secret:

To obtain a client ID and secret for using our API, you will need to create an account on the Faceki dashboard by visiting the following URL: https://apps.faceki.com/.

# Tutorial For Client ID and Secret:

URL: https://docs.faceki.com/quick-guides/api-credentials


# Getting Started:

## General Requirements
The minimum requirements for the SDK are:
*	Android 5.0 (API level 21) or higher
*	Internet connection


## Permissions
Required permissions are linked automatically by the SDK.


## Integration by sdk
Use the SDK in your application by including the Maven repositories with the following `build.gradle` configuration in Android Studio:

```
repositories {
	...
	maven { url 'https://jitpack.io' }
}
```


## Integration by library
Use the Library in your application by implemention lib with the following `build.gradle` project module in Android Studio:

```
	implementation 'com.github.faceki:faceki-kyc-android-revamp:Tag'

```
or

```
    implementation("com.github.faceki:faceki-kyc-android-revamp:Tag")

```

and including the Maven repositories with the following `build.gradle` configuration in Android Studio:

```
repositories {
	...
	maven { url 'https://jitpack.io' }
}
```

## Example


```
import com.faceki.android.FaceKi
```

## Kotlin

```
     FaceKi.startKycVerification(
                context = this@MainActivity,
                clientId = TEST_CLIENT_ID,
                clientSecret = TEST_CLIENT_SECRET,
                workflowId = TEST_WORKFLOW_ID,
                recordIdentifier = TEST_RECORD_IDENTIFIER,
                kycResponseHandler = kycResponseHandler
            )

//custom logo
FaceKi.setCustomIcons(
                iconMap = hashMapOf(
                    FaceKi.IconElement.Logo to FaceKi.IconValue.Resource(R.drawable.logo)
                )
            )

//custom colors
FaceKi.setCustomColors(
                colorMap = hashMapOf(
                    FaceKi.ColorElement.BackgroundColor to FaceKi.ColorValue.StringColor("#FFFFFF")
                )
            )
```
##### To Get the response back from the SDK.


```
    private val kycResponseHandler: KycResponseHandler = object : KycResponseHandler {
        override fun handleKycResponse(
            json: String?,
            result: VerificationResult
        ) {
            when (result) {
                is VerificationResult.ResultOk -> {
                    Toast.makeText(this@MainActivity, "ResultOk", Toast.LENGTH_SHORT).show()
                }

                is VerificationResult.ResultCanceled -> {
                    Toast.makeText(this@MainActivity, "ResultCanceled", Toast.LENGTH_SHORT).show()
                }
            }

            Toast.makeText(this@MainActivity, "kycResponseHandler $json", Toast.LENGTH_SHORT).show()
        }
    }
```

## Java

```
import java.util.HashMap;

// Inside your MainActivity class

// Call startKycVerification
FaceKi.startKycVerification(
        this,
        TEST_CLIENT_ID,
        TEST_CLIENT_SECRET,
        TEST_WORKFLOW_ID,
        TEST_RECORD_IDENTIFIER,
        kycResponseHandler
);

// Set custom logo
HashMap<FaceKi.IconElement, FaceKi.IconValue> iconMap = new HashMap<>();
iconMap.put(FaceKi.IconElement.Logo, new FaceKi.IconValue.Resource(R.drawable.logo));
FaceKi.setCustomIcons(iconMap);

// Set custom colors
HashMap<FaceKi.ColorElement, FaceKi.ColorValue> colorMap = new HashMap<>();
colorMap.put(FaceKi.ColorElement.BackgroundColor, new FaceKi.ColorValue.StringColor("#FFFFFF"));
FaceKi.setCustomColors(colorMap);

// Define KycResponseHandler
private KycResponseHandler kycResponseHandler = new KycResponseHandler() {
    @Override
    public void handleKycResponse(String json, VerificationResult result) {
        if (result instanceof VerificationResult.ResultOk) {
            Toast.makeText(MainActivity.this, "ResultOk", Toast.LENGTH_SHORT).show();
        } else if (result instanceof VerificationResult.ResultCanceled) {
            Toast.makeText(MainActivity.this, "ResultCanceled", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(MainActivity.this, "kycResponseHandler " + json, Toast.LENGTH_SHORT).show();
    }
};
```

# Custom colors

Use `setCustomColors` to customize the color scheme.

## Kotlin

```
val colorMap = hashMapOf(
    FaceKi.ColorElement.ButtonBackgroundColor to FaceKi.ColorValue.IntColor(myColorInt),
    // Add other elements as needed
)
FaceKi.setCustomColors(colorMap)
```

## Java

```
HashMap<FaceKi.ColorElement, FaceKi.ColorValue> colorMap = new HashMap<>();
        colorMap.put(FaceKi.ColorElement.BackgroundColor, new FaceKi.ColorValue.StringColor("#FFFFFF"));
        FaceKi.setCustomColors(colorMap);
```

# Custom logo

Use `setCustomIcons` to customize the icons.

## Kotlin

```
val iconMap = hashMapOf(
    FaceKi.IconElement.Logo to FaceKi.IconValue.Resource(myDrawableResId),
    // Add other elements as needed
)
FaceKi.setCustomIcons(iconMap)
```

## Java

```
HashMap<FaceKi.IconElement, FaceKi.IconValue> iconMap = new HashMap<>();
        iconMap.put(FaceKi.IconElement.Logo, new FaceKi.IconValue.Resource(R.drawable.ic_launcher_background));
        FaceKi.setCustomIcons(iconMap);
```


### Response Handling

- The response from KYC verification is a plain JSON object.
- You can convert this response into a JSON object using serialization libraries like Gson or Moshi.

## Methods

- **startKycVerification**: Initiates the KYC verification process.
- **setCustomColors**: Customizes the colors of various UI elements.
- **setCustomIcons**: Customizes the icons used in the UI.

## Enums and Sealed Classes

### ColorElement

- Enum defining different UI elements that can have their colors customized.

### ColorValue

- Sealed class representing a color value.
- Types:
    - `IntColor`: Represents color as an integer.
    - `StringColor`: Represents color as a string (e.g., "#FFFFFF").

### IconElement

- Enum defining different UI elements that can have their icons customized.

### IconValue

- Sealed class representing an icon value.
- Types:
    - `Resource`: Represents an icon as a resource ID.
    - `Url`: Represents an icon as a URL.