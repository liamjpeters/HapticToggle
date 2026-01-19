package uk.co.liampeters.haptictoggle

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.uiAutomator
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


// This test runner helps to collect screenshots of the app end to end.
// The tests should be run on a full play store google pixel launcher emulator.
// The quick settings menu should be cleaned up (the minimum 6 tiles, all made small) before
// collecting. This helps to showcase the apps quick tile entry.
@RunWith(AndroidJUnit4::class)
class GenerateScreenshots {

    // This 'locale' is simply the folder in the SDCard/Pictures folder where screenshots will be saved.
    val locale = "English"
    val path = "/sdcard/Pictures/$locale"

    @Before
    fun setup() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Set the quick setting shade to just the 6 defaults (at least 6 must be here)
        device.executeShellCommand("cmd statusbar set-tiles internet,bt,dark,flashlight,dnd,airplane")
        // Disable bluetooth. Demo mode disables internet. So if we ensure bluetooth is disabled,
        // our tile will be the only highlighted one in the screenshots.
        device.executeShellCommand("cmd bluetooth_manager disable")

        // Enable Demo Mode
        device.executeShellCommand("settings put global sysui_demo_allowed 1")

        // Enter Demo Mode
        device.executeShellCommand("am broadcast -a com.android.systemui.demo -e command enter")

        // Set the clock to 12:00
        device.executeShellCommand("am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1200")

        // Set the device plugged in and charging
        device.executeShellCommand("am broadcast -a com.android.systemui.demo -e command battery -e level 100 -e plugged true")

        // Delete the screenshots target folder if it exists (-r recursive, -f force)
        device.executeShellCommand("rm -rf $path")

        //Create a fresh directory
        device.executeShellCommand("mkdir -p $path")

        // Default Haptics to off each time
        device.executeShellCommand("settings put system vibrate_on_touch 0")
        device.executeShellCommand("settings put system haptic_feedback_enabled 0")

    }

    @Test
    fun capture() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val appPackageName = "uk.co.liampeters.haptictoggle"
        uiAutomator {
            // Start the app
            startApp(appPackageName)

            // Wait for the app to be visible and to stabilise
            waitForAppToBeVisible(appPackageName)
            waitForStableInActiveWindow()

            // Take a screenshot of the unactioned permission request
            captureScreenshot("permission_request")

            // Find and click the grant permission button then wait for things to stabilise
            onElement {
                contentDescription == "grant permission button"
            }.click()
            waitForStableInActiveWindow()

            // Take a screenshot of the settings screen (unchecked)
            captureScreenshot("permission_settings_unchecked")

            // Find the checkable item in the settings screen and click it then wait for things to
            // stabilise
            onElement {
                isCheckable
            }.click()
            waitForStableInActiveWindow()

            // Take a screenshot of the settings screen (checked)
            captureScreenshot("permission_settings_checked")

            // Press back and wait for things to stabilise
            device.pressBack()
            waitForStableInActiveWindow()

            // Take a screenshot of the success screen (with the button unadded)
            captureScreenshot("success_add_tile")

            // Find and click the add quick tile button then wait for things to stabilise
            onElement {
                contentDescription == "add to quick settings button"
            }.click()
            waitForStableInActiveWindow()

            // Take a screenshot of the dialog prompt
            captureScreenshot("dialog_quick_tile_add")

            // When the dialog is up, the "first" button should be the "add" button, so click it
            // then wait for things to stabilise
            onElements { isClickable }.first().click()
            waitForStableInActiveWindow()

            // Open the quick settings menu
            device.openQuickSettings()
            waitForStableInActiveWindow()

            onElement {
                viewIdResourceName == "com.android.systemui:id/qs_edit_mode_button"
            }.click()
            waitForStableInActiveWindow()

            // Find and click the add quick tile button then wait for things to stabilise
            onElement {
                contentDescription == "HapticToggleTile" || contentDescription == "Haptics"
            }.longClick()
            waitForStableInActiveWindow()

            val hapticToggleTile = onElement {
                contentDescription == "HapticToggleTile" || contentDescription == "Haptics"
            }

            if (locale == "Arabic") {
                // RTL
                device.drag(
                    hapticToggleTile.visibleBounds.left,
                    hapticToggleTile.visibleBounds.centerY(),
                    hapticToggleTile.visibleBounds.left - hapticToggleTile.visibleBounds.width(),
                    hapticToggleTile.visibleBounds.centerY(),
                    100
                )
            } else {
                device.drag(
                    hapticToggleTile.visibleBounds.right,
                    hapticToggleTile.visibleBounds.centerY(),
                    hapticToggleTile.visibleBounds.right + hapticToggleTile.visibleBounds.width(),
                    hapticToggleTile.visibleBounds.centerY(),
                    100
                )
            }


            Thread.sleep(1000)
            pressBack()

            Thread.sleep(1000)

            // Take a screenshot of the dialog prompt
            captureScreenshot("quick_settings_tile_untoggled")

            Thread.sleep(1000)

            onElements {
                viewIdResourceName == "com.android.systemui:id/qs_tile_large"
            }.last().click()

            waitForStableInActiveWindow()

            // Take a screenshot of the dialog prompt
            captureScreenshot("quick_settings_tile_toggled")

            onElements {
                viewIdResourceName == "com.android.systemui:id/qs_tile_large"
            }.last().click()
            waitForStableInActiveWindow()

            device.pressBack()
            waitForStableInActiveWindow()
            device.pressBack()
            waitForStableInActiveWindow()
        }
    }

    fun captureScreenshot(fileName: String) {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val file = File("$path/$fileName.png")

        device.takeScreenshot(file)
    }

    @After
    fun reset() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        // Exit Demo mode
        device.executeShellCommand("am broadcast -a com.android.systemui.demo -e command exit")
    }

}