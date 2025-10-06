# Launcher Icons Setup

This Android app requires launcher icons in multiple densities. The app currently uses adaptive icons which are defined in:

- `res/mipmap-anydpi-v26/ic_launcher.xml` - Adaptive icon for API 26+
- `res/drawable/ic_launcher_foreground.xml` - Foreground layer (cloud sync icon)

## Using Android Studio Asset Studio (Recommended)

To generate proper launcher icons:

1. In Android Studio, right-click on `res` folder
2. Select **New > Image Asset**
3. Choose **Launcher Icons (Adaptive and Legacy)**
4. Configure your icon:
   - **Foreground Layer**: Choose an icon or upload a custom image
   - **Background Layer**: Use color `#1976D2` (primary blue)
   - **Icon Shape**: Any (user's device will apply their preferred shape)
5. Click **Next** then **Finish**

This will automatically generate icons for all required densities:
- mipmap-mdpi (48x48)
- mipmap-hdpi (72x72)
- mipmap-xhdpi (96x96)
- mipmap-xxhdpi (144x144)
- mipmap-xxxhdpi (192x192)

## Manual Icon Creation

If you prefer to create icons manually:

### Required Sizes

Create PNG icons with transparency for each density:

- **mdpi**: 48x48 px
- **hdpi**: 72x72 px
- **xhdpi**: 96x96 px
- **xxhdpi**: 144x144 px
- **xxxhdpi**: 192x192 px

### Icon Design Guidelines

- Use a cloud with sync arrows or similar icon representing cloud synchronization
- Keep the design simple and recognizable at small sizes
- Use the app's primary color (#1976D2) as the main color
- Ensure 16dp of padding around the icon for the safe zone
- Test the icon on different launcher backgrounds

### File Names

- `ic_launcher.png` - Standard launcher icon
- `ic_launcher_round.png` - Round launcher icon (for launchers that support it)

Place these files in the corresponding `res/mipmap-*dpi/` folders.

## Current Implementation

The app currently uses:
- **Adaptive icons** for Android 8.0+ (API 26+)
- **Vector drawable** foreground layer that displays a cloud sync symbol
- **Color background** using the app's primary color

The icons will render correctly on modern devices, but for production you should generate proper bitmap icons using Android Studio's Asset Studio.
