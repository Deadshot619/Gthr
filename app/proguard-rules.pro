# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#https://github.com/square/retrofit/issues/3234

-keep class com.gthr.gthrcollect.model.** { *; }
-keep class com.gthr.gthrcollect.utils.enums.** { *; }
-keep class com.gthr.gthrcollect.utils.customviews.** { *; }


############ Retrofit##########
##### https://github.com/square/retrofit/blob/master/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

############ okhttp3 ##########
#### https://github.com/square/okhttp/blob/master/okhttp/src/main/resources/META-INF/proguard/okhttp3.pro

-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.conscrypt.ConscryptHostnameVerifier

############ okio ##########
#### https://github.com/square/okio/blob/master/okio/src/jvmMain/resources/META-INF/proguard/okio.pro

-dontwarn org.codehaus.mojo.animal_sniffer.*

######### Glide ##########
#### https://github.com/bumptech/glide/blob/master/library/proguard-rules.txt

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

########## Coroutines ##########
#### https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/kotlinx-coroutines-android/resources/META-INF/com.android.tools/r8-from-1.6.0/coroutines.pro

-assumenosideeffects class kotlinx.coroutines.internal.MainDispatcherLoader {
    boolean FAST_SERVICE_LOADER_ENABLED return false;
}
-assumenosideeffects class kotlinx.coroutines.internal.FastServiceLoaderKt {
    boolean ANDROID_DETECTED return true;
}
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory {*;}
-assumenosideeffects class kotlinx.coroutines.internal.MainDispatchersKt {
    boolean SUPPORT_MISSING return false;
}
-assumenosideeffects class kotlinx.coroutines.DebugKt {
    boolean getASSERTIONS_ENABLED() return false;
    boolean getDEBUG() return false;
    boolean getRECOVER_STACK_TRACES() return false;
}

########### Google Gson ##########
#### https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg

-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

########## Dexter ##########
###### https://github.com/Karumi/Dexter/blob/master/dexter/proguard-rules.pro

-keepattributes InnerClasses, Signature, *Annotation*
-keep class com.karumi.dexter.** { *; }
-keep interface com.karumi.dexter.** { *; }
-keepclasseswithmembernames class com.karumi.dexter.** { *; }
-keepclasseswithmembernames interface com.karumi.dexter.** { *; }

########## Algolia ##########
##### https://github.com/algolia/algoliasearch-client-kotlin/blob/develop/client/src/jvmMain/resources/META-INF/proguard/algoliasearch.pro

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.algolia.search.model.**$$serializer { *; }
-keepclassmembers class com.algolia.search.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.algolia.search.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

########## kotlinx-serialization-json ##########
#####  https://github.com/Kotlin/kotlinx.serialization

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.yourcompany.yourpackage.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class com.yourcompany.yourpackage.** { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class com.yourcompany.yourpackage.** { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}

########## material-components ##########
###### https://github.com/material-components/material-components-android/blob/master/lib/proguard-behaviors.pro

-keep public class * extends androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>();
}
-keepattributes RuntimeVisible*Annotation*