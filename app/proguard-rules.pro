# Add project specific ProGuard rules here.
# Keep Ktor classes
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { *; }

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.memoryproject.app.**$$serializer { *; }
-keepclassmembers class com.memoryproject.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.memoryproject.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}
