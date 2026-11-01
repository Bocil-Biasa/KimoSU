// Mod by Nixz • Dev
package com.kimo.nixz.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Dock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.kimo.nixz.ui.LocalNavBarEnabled
import com.kimo.nixz.ui.LocalScrollState
import com.kimo.nixz.ui.rememberScrollConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.dropUnlessResumed
import com.kimo.nixz.ui.MainActivity
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.kimo.nixz.Natives
import com.kimo.nixz.R
import com.kimo.nixz.ui.component.SwitchItem
import com.kimo.nixz.ui.theme.PRESET_ACCENT_COLORS
import com.kimo.nixz.ui.theme.parseHexColor
import com.kimo.nixz.ui.theme.toHexString
import com.kimo.nixz.ui.util.refreshActivity
import com.kimo.nixz.ui.util.LocalSnackbarHost
import com.kimo.nixz.ui.util.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun CustomizationScreen(navigator: DestinationsNavigator) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val bottomBarScrollState = LocalScrollState.current
    val bottomBarScrollConnection = if (bottomBarScrollState != null) {
        rememberScrollConnection(
            isScrollingDown = bottomBarScrollState.isScrollingDown,
            scrollOffset = bottomBarScrollState.scrollOffset,
            previousScrollOffset = bottomBarScrollState.previousScrollOffset,
            threshold = 30f
        )
    } else null
    val snackBarHost = LocalSnackbarHost.current

    val isManager = Natives.isManager
    val ksuVersion = if (isManager) Natives.version else null

    val scrollState = LocalScrollState.current
    val navBarEnabled = LocalNavBarEnabled.current
    val isNavBarHidden = (scrollState?.isScrollingDown?.value ?: false) || (navBarEnabled?.value == false)
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + if (isNavBarHidden) 0.dp else 112.dp

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    var currentAppLocale by remember {
        mutableStateOf(LocaleHelper.getCurrentAppLocale(context))
    }
    var showLanguageSheet by remember { mutableStateOf(false) }

    var currentStyling by rememberSaveable {
        mutableStateOf(prefs.getString("styling", "Default") ?: "Default")
    }
    var showStylingSheet by remember { mutableStateOf(false) }

    var currentAccentColor by rememberSaveable {
        mutableStateOf(prefs.getString("accent_color", "") ?: "")
    }
    var showAccentSheet by remember { mutableStateOf(false) }

    var currentRippleColor by rememberSaveable {
        mutableStateOf(prefs.getString("ripple_color", "") ?: "")
    }
    var showRippleSheet by remember { mutableStateOf(false) }

    var currentDarkVariant by rememberSaveable {
        mutableStateOf(prefs.getString("dark_variant", "Default") ?: "Default")
    }
    var showDarkVariantSheet by remember { mutableStateOf(false) }

    var currentCornerRadius by rememberSaveable {
        mutableIntStateOf(prefs.getInt("corner_radius", 16))
    }
    var showCornerRadiusSheet by remember { mutableStateOf(false) }

    var currentTransitionStyle by rememberSaveable {
        mutableStateOf(prefs.getString("transition_style", "Fluid") ?: "Fluid")
    }
    var showTransitionStyleSheet by remember { mutableStateOf(false) }

    var currentTransitionSpeed by rememberSaveable {
        mutableIntStateOf(prefs.getInt("transition_speed", 320))
    }
    var showTransitionSpeedSheet by remember { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        currentAppLocale = LocaleHelper.getCurrentAppLocale(context)
    }

    val languageOptions = remember(context) {
        listOf(
            LocaleHelper.SYSTEM_LANGUAGE_TAG to context.getString(R.string.system_default)
        ) + LocaleHelper.getSupportedLocales(context)
            .map { it.toLanguageTag() to it.getDisplayName(it) }
            .sortedBy { (_, displayName) -> displayName }
    }
    val currentLanguageTag = currentAppLocale?.toLanguageTag()
        ?: LocaleHelper.SYSTEM_LANGUAGE_TAG

    Scaffold(
        topBar = {
            TopBar(
                onBack = dropUnlessResumed {
                    navigator.popBackStack()
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackBarHost, modifier = Modifier.padding(bottom = navBarPadding)) },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { paddingValues ->
    
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .let { modifier ->
                    if (bottomBarScrollConnection != null) {
                        modifier
                            .nestedScroll(bottomBarScrollConnection)
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    } else {
                        modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    }
                }
                .verticalScroll(rememberScrollState())
        ) {

            val language = stringResource(id = R.string.settings_language)
            val currentLanguageDisplay = currentAppLocale?.let { it.getDisplayName(it) }
                ?: stringResource(R.string.system_default)

            ListItem(
                leadingContent = { Icon(Icons.Filled.Translate, language) },
                headlineContent = { Text(
                    text = language,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                ) },
                supportingContent = { Text(currentLanguageDisplay) },
                modifier = Modifier.clickable {
                    showLanguageSheet = true
                }
            )

            val styling = stringResource(id = R.string.settings_styling)
            val stylingDisplay = if (currentStyling == "MiuiX") "MiuiX (HyperOS Squircle)" else "Default (Material 3)"
            ListItem(
                leadingContent = { Icon(Icons.Filled.Style, styling) },
                headlineContent = { Text(
                    text = styling,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                ) },
                supportingContent = { Text(stylingDisplay) },
                modifier = Modifier.clickable {
                    showStylingSheet = true
                }
            )

            val cornerRadiusTitle = stringResource(id = R.string.settings_corner_radius)
            val cornerRadiusDisplay = when (currentCornerRadius) {
                8 -> "Sharp (8 dp)"
                24 -> "MiuiX Squircle (24 dp)"
                32 -> "Pill (32 dp)"
                else -> "Default (16 dp)"
            }
            ListItem(
                leadingContent = { Icon(Icons.Filled.RoundedCorner, cornerRadiusTitle) },
                headlineContent = { Text(
                    text = cornerRadiusTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                ) },
                supportingContent = { Text(cornerRadiusDisplay) },
                modifier = Modifier.clickable {
                    showCornerRadiusSheet = true
                }
            )

            if (isSystemInDarkTheme()) {
                val darkVariantTitle = stringResource(id = R.string.settings_dark_variant)
                val darkVariantDisplay = when (currentDarkVariant) {
                    "Amoled" -> "AMOLED Black"
                    "Midnight" -> "Midnight Navy"
                    "Mocha" -> "Catppuccin Mocha"
                    else -> "Default Charcoal"
                }
                ListItem(
                    leadingContent = { Icon(Icons.Filled.Contrast, darkVariantTitle) },
                    headlineContent = { Text(
                        text = darkVariantTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    ) },
                    supportingContent = { Text(darkVariantDisplay) },
                    modifier = Modifier.clickable {
                        showDarkVariantSheet = true
                    }
                )
            }

            val accentColorTitle = stringResource(id = R.string.settings_accent_color)
            val parsedAccent = remember(currentAccentColor) { parseHexColor(currentAccentColor) }
            ListItem(
                leadingContent = { Icon(Icons.Filled.Palette, accentColorTitle) },
                headlineContent = {
                    Text(
                        text = accentColorTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                supportingContent = {
                    Text(
                        text = if (currentAccentColor.isNotBlank()) currentAccentColor else stringResource(R.string.reset_default)
                    )
                },
                trailingContent = {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(parsedAccent ?: MaterialTheme.colorScheme.primary)
                            .border(
                                width = 1.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = CircleShape
                            )
                    )
                },
                modifier = Modifier.clickable {
                    showAccentSheet = true
                }
            )

            val rippleColorTitle = stringResource(id = R.string.settings_ripple_color)
            val parsedRipple = remember(currentRippleColor) { parseHexColor(currentRippleColor) }
            ListItem(
                leadingContent = { Icon(Icons.Filled.TouchApp, rippleColorTitle) },
                headlineContent = {
                    Text(
                        text = rippleColorTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                supportingContent = {
                    Text(
                        text = if (currentRippleColor.isNotBlank()) currentRippleColor else stringResource(R.string.reset_default)
                    )
                },
                trailingContent = {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(parsedRipple ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.38f))
                            .border(
                                width = 1.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = CircleShape
                            )
                    )
                },
                modifier = Modifier.clickable {
                    showRippleSheet = true
                }
            )

            val transitionTitle = stringResource(id = R.string.settings_transition_style)
            val transitionDisplay = when (currentTransitionStyle) {
                "Spring" -> "Bouncy Spring"
                "Fade" -> "Smooth Fade"
                "Slide" -> "Classic Slide"
                else -> "Fluid (Scale & Slide)"
            }
            ListItem(
                leadingContent = { Icon(Icons.Filled.Animation, transitionTitle) },
                headlineContent = { Text(
                    text = transitionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                ) },
                supportingContent = { Text(transitionDisplay) },
                modifier = Modifier.clickable {
                    showTransitionStyleSheet = true
                }
            )

            val speedTitle = stringResource(id = R.string.settings_transition_speed)
            val speedDisplay = when (currentTransitionSpeed) {
                220 -> "Fast (220 ms)"
                450 -> "Relaxed (450 ms)"
                else -> "Normal (320 ms)"
            }
            ListItem(
                leadingContent = { Icon(Icons.Filled.Speed, speedTitle) },
                headlineContent = { Text(
                    text = speedTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                ) },
                supportingContent = { Text(speedDisplay) },
                modifier = Modifier.clickable {
                    showTransitionSpeedSheet = true
                }
            )

            var useBanner by rememberSaveable {
                mutableStateOf(
                    prefs.getBoolean("use_banner", true)
                )
            }
            if (ksuVersion != null) {
                SwitchItem(
                    icon = Icons.Filled.ViewCarousel,
                    title = stringResource(id = R.string.settings_banner),
                    summary = stringResource(id = R.string.settings_banner_summary),
                    checked = useBanner
                ) {
                    prefs.edit { putBoolean("use_banner", it) }
                    useBanner = it
                }
            }

            var enableNavBar by rememberSaveable {
                mutableStateOf(
                    prefs.getBoolean("enable_navbar", true)
                )
            }
            val activity = LocalContext.current as? MainActivity
            SwitchItem(
                icon = Icons.Filled.Dock,
                title = stringResource(id = R.string.settings_navbar),
                summary = stringResource(id = R.string.settings_navbar_summary),
                checked = enableNavBar
            ) { checked ->
                activity?.setNavBarEnabled(checked)
                enableNavBar = checked
            }

            var enableStartupAnimation by rememberSaveable {
                mutableStateOf(prefs.getBoolean("enable_startup_animation", true))
            }
            SwitchItem(
                icon = Icons.Filled.Animation,
                title = stringResource(id = R.string.settings_startup_animation),
                summary = stringResource(id = R.string.settings_startup_animation_summary),
                checked = enableStartupAnimation
            ) { checked ->
                prefs.edit { putBoolean("enable_startup_animation", checked) }
                enableStartupAnimation = checked
            }
        }
    }

    if (showLanguageSheet) {
        LanguageBottomSheet(
            options = languageOptions,
            selectedLanguageTag = currentLanguageTag,
            onDismiss = { showLanguageSheet = false },
            onLanguageSelected = { languageTag ->
                showLanguageSheet = false
                if (languageTag != currentLanguageTag) {
                    LocaleHelper.setAppLocale(context, languageTag)
                    currentAppLocale = LocaleHelper.getCurrentAppLocale(context)
                    if (!LocaleHelper.usesFrameworkLocaleManager) {
                        refreshActivity(context)
                    }
                }
            }
        )
    }

    if (showStylingSheet) {
        StylingBottomSheet(
            selectedStyle = currentStyling,
            onDismiss = { showStylingSheet = false },
            onStyleSelected = { style ->
                showStylingSheet = false
                if (style != currentStyling) {
                    currentStyling = style
                    prefs.edit { putString("styling", style) }
                    (context as? MainActivity)?.setStyling(style)
                }
            }
        )
    }

    if (showDarkVariantSheet) {
        DarkVariantBottomSheet(
            selectedVariant = currentDarkVariant,
            onDismiss = { showDarkVariantSheet = false },
            onVariantSelected = { variant ->
                showDarkVariantSheet = false
                if (variant != currentDarkVariant) {
                    currentDarkVariant = variant
                    prefs.edit { putString("dark_variant", variant) }
                    (context as? MainActivity)?.setDarkVariant(variant)
                }
            }
        )
    }

    if (showCornerRadiusSheet) {
        CornerRadiusBottomSheet(
            selectedRadius = currentCornerRadius,
            onDismiss = { showCornerRadiusSheet = false },
            onRadiusSelected = { radius ->
                showCornerRadiusSheet = false
                if (radius != currentCornerRadius) {
                    currentCornerRadius = radius
                    prefs.edit { putInt("corner_radius", radius) }
                    (context as? MainActivity)?.setCornerRadius(radius)
                }
            }
        )
    }

    if (showTransitionStyleSheet) {
        TransitionStyleBottomSheet(
            selectedStyle = currentTransitionStyle,
            onDismiss = { showTransitionStyleSheet = false },
            onStyleSelected = { style ->
                showTransitionStyleSheet = false
                if (style != currentTransitionStyle) {
                    currentTransitionStyle = style
                    prefs.edit { putString("transition_style", style) }
                    (context as? MainActivity)?.setTransitionStyle(style)
                }
            }
        )
    }

    if (showTransitionSpeedSheet) {
        TransitionSpeedBottomSheet(
            selectedSpeed = currentTransitionSpeed,
            onDismiss = { showTransitionSpeedSheet = false },
            onSpeedSelected = { speed ->
                showTransitionSpeedSheet = false
                if (speed != currentTransitionSpeed) {
                    currentTransitionSpeed = speed
                    prefs.edit { putInt("transition_speed", speed) }
                    (context as? MainActivity)?.setTransitionSpeed(speed)
                }
            }
        )
    }

    if (showAccentSheet) {
        ColorPickerBottomSheet(
            title = stringResource(R.string.select_accent_color),
            selectedHex = currentAccentColor,
            onDismiss = { showAccentSheet = false },
            onColorSelected = { hex ->
                showAccentSheet = false
                currentAccentColor = hex
                prefs.edit { putString("accent_color", hex) }
                (context as? MainActivity)?.setAccentColor(hex)
            }
        )
    }

    if (showRippleSheet) {
        ColorPickerBottomSheet(
            title = stringResource(R.string.select_ripple_color),
            selectedHex = currentRippleColor,
            onDismiss = { showRippleSheet = false },
            onColorSelected = { hex ->
                showRippleSheet = false
                currentRippleColor = hex
                prefs.edit { putString("ripple_color", hex) }
                (context as? MainActivity)?.setRippleColor(hex)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageBottomSheet(
    options: List<Pair<String, String>>,
    selectedLanguageTag: String,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = stringResource(R.string.select_language),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .semantics { heading() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .selectableGroup(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(options, key = { (tag, _) -> tag }) { (tag, displayName) ->
                val selected = tag == selectedLanguageTag
                ListItem(
                    headlineContent = { Text(displayName) },
                    trailingContent = {
                        RadioButton(selected = selected, onClick = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = { onLanguageSelected(tag) }
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StylingBottomSheet(
    selectedStyle: String,
    onDismiss: () -> Unit,
    onStyleSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val options = listOf(
        "Default" to "Material 3 / Pixel standard with dynamic surfaces",
        "MiuiX" to "HyperOS & MIUI Squircle with high-contrast surfaces"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = stringResource(R.string.select_styling),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .semantics { heading() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .selectableGroup(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(options, key = { it.first }) { (style, description) ->
                val selected = style == selectedStyle
                ListItem(
                    headlineContent = { Text(style, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(description) },
                    trailingContent = {
                        RadioButton(selected = selected, onClick = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = { onStyleSelected(style) }
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DarkVariantBottomSheet(
    selectedVariant: String,
    onDismiss: () -> Unit,
    onVariantSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val options = listOf(
        "Default" to "Default Charcoal (Standard Material Dark)",
        "Amoled" to "Pure AMOLED Black (#000000)",
        "Midnight" to "Midnight Navy (Deep Cybernetic #0A0E17)",
        "Mocha" to "Catppuccin Mocha (#1E1E2E)"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = stringResource(R.string.settings_dark_variant),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .semantics { heading() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .selectableGroup(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(options, key = { it.first }) { (variant, description) ->
                val selected = variant == selectedVariant
                ListItem(
                    headlineContent = { Text(variant, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(description) },
                    trailingContent = {
                        RadioButton(selected = selected, onClick = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = { onVariantSelected(variant) }
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CornerRadiusBottomSheet(
    selectedRadius: Int,
    onDismiss: () -> Unit,
    onRadiusSelected: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val options = listOf(
        16 to "Default Round (16 dp)",
        24 to "MiuiX Squircle (24 dp)",
        32 to "Pill Shape (32 dp)",
        8 to "Sharp / Minimal (8 dp)"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = stringResource(R.string.settings_corner_radius),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .semantics { heading() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .selectableGroup(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(options, key = { it.first }) { (radius, label) ->
                val selected = radius == selectedRadius
                ListItem(
                    headlineContent = { Text(label, fontWeight = FontWeight.SemiBold) },
                    trailingContent = {
                        RadioButton(selected = selected, onClick = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = { onRadiusSelected(radius) }
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransitionStyleBottomSheet(
    selectedStyle: String,
    onDismiss: () -> Unit,
    onStyleSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val options = listOf(
        "Fluid" to "Fluid (Scale & Slide) - Modern fluid motion",
        "Spring" to "Bouncy Spring - Energetic tactile bounce",
        "Fade" to "Smooth Fade - Clean crossfade",
        "Slide" to "Classic Slide - Traditional horizontal slide"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = stringResource(R.string.settings_transition_style),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .semantics { heading() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .selectableGroup(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(options, key = { it.first }) { (style, description) ->
                val selected = style == selectedStyle
                ListItem(
                    headlineContent = { Text(style, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(description) },
                    trailingContent = {
                        RadioButton(selected = selected, onClick = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = { onStyleSelected(style) }
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransitionSpeedBottomSheet(
    selectedSpeed: Int,
    onDismiss: () -> Unit,
    onSpeedSelected: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val options = listOf(
        220 to "Fast (220 ms) - Snappy and instantaneous",
        320 to "Normal (320 ms) - Balanced and natural",
        450 to "Relaxed (450 ms) - Smooth cinematic feel"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = stringResource(R.string.settings_transition_speed),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .semantics { heading() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .selectableGroup(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(options, key = { it.first }) { (speed, description) ->
                val selected = speed == selectedSpeed
                ListItem(
                    headlineContent = { Text(description.substringBefore(" -"), fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text(description.substringAfter("- ")) },
                    trailingContent = {
                        RadioButton(selected = selected, onClick = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = { onSpeedSelected(speed) }
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorPickerBottomSheet(
    title: String,
    selectedHex: String,
    onDismiss: () -> Unit,
    onColorSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var customInput by rememberSaveable { mutableStateOf(selectedHex) }
    val parsedInput = remember(customInput) { parseHexColor(customInput) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .semantics { heading() }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = customInput,
                    onValueChange = { customInput = it.trim() },
                    label = { Text(stringResource(R.string.custom_hex_color)) },
                    placeholder = { Text(stringResource(R.string.hex_placeholder)) },
                    singleLine = true,
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(parsedInput ?: MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        parsedInput?.let {
                            onColorSelected(it.toHexString())
                        }
                    },
                    enabled = parsedInput != null
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .selectableGroup()
            ) {
                item {
                    val isDefaultSelected = selectedHex.isBlank()
                    ListItem(
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            )
                        },
                        headlineContent = { Text(stringResource(R.string.reset_default)) },
                        trailingContent = {
                            RadioButton(selected = isDefaultSelected, onClick = null)
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isDefaultSelected,
                                role = Role.RadioButton,
                                onClick = { onColorSelected("") }
                            )
                    )
                }

                items(PRESET_ACCENT_COLORS, key = { it.first }) { (name, color) ->
                    val hex = color.toHexString()
                    val isSelected = selectedHex.equals(hex, ignoreCase = true)
                    ListItem(
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            )
                        },
                        headlineContent = { Text(name) },
                        supportingContent = { Text(hex) },
                        trailingContent = {
                            RadioButton(selected = isSelected, onClick = null)
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                role = Role.RadioButton,
                                onClick = { onColorSelected(hex) }
                            )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBack: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = { Text(
                text = stringResource(R.string.customization),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
            ) }, navigationIcon = {
            IconButton(
                onClick = onBack
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) }
        },
        windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
        scrollBehavior = scrollBehavior
    )
}

@Preview
@Composable
private fun CustomizationPreview() {
    CustomizationScreen(EmptyDestinationsNavigator)
}
