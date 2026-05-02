# 🌿 Hasta-Shilpa — Design Bridge for Artisans

**Android App | Bamboo & Cane Artisan Modernization Platform**
*Western Ghats Traditional Craft + Modern Market Aesthetics*

---

## 📱 App Overview

Hasta-Shilpa is a design-innovation app for bamboo and cane artisans in the Western Ghats. It bridges traditional craftsmanship with modern urban market demand.

### 4 Core Modules:
| Screen | Purpose |
|---|---|
| 🌿 **Design Trends** | Feed of modern bamboo products with search & filter |
| 📐 **Blueprint Library** | Zoomable technical drawings with exact measurements |
| 📋 **Material Tracker** | Log bamboo poles, cane, hours, and units per batch |
| 🛒 **Marketplace** | Simulated product listing with price suggester |

---

## 🚀 Setup Instructions

### Step 1 — Open in Android Studio
1. Extract the ZIP file
2. Open Android Studio → File → Open → Select `HastaShilpa` folder
3. Wait for Gradle sync to complete

### Step 2 — Add Poppins Fonts (Required)
Download from https://fonts.google.com/specimen/Poppins and place in:
```
app/src/main/res/font/
├── poppins_regular.ttf     (Weight 400)
├── poppins_medium.ttf      (Weight 500)
├── poppins_semibold.ttf    (Weight 600)
└── poppins_bold.ttf        (Weight 700)
```
> The project ships with DejaVu Sans placeholders. Replace with Poppins for the intended look.

### Step 3 — Build & Run
- Connect Android device (API 24+) or launch emulator
- Click ▶ Run in Android Studio

---

## 🏗 Project Structure

```
app/src/main/java/com/hastashilpa/app/
├── SplashActivity.java            — Animated launch screen
├── MainActivity.java              — Bottom navigation host
├── BlueprintDetailActivity.java   — Pinch-to-zoom blueprint viewer
├── PriceSuggesterActivity.java    — Full price calculator
├── MarketplaceListActivity.java   — Product listing form
├── fragments/
│   ├── TrendsFragment.java        — Design feed + search + filter
│   ├── BlueprintsFragment.java    — 2-column grid
│   ├── TrackerFragment.java       — Material batch logging
│   └── MarketplaceFragment.java   — Listings + price shortcut
├── adapters/
│   ├── FeaturedPagerAdapter.java  — ViewPager2 hero cards
│   ├── DesignCardAdapter.java     — Trend list cards
│   ├── BlueprintGridAdapter.java  — Blueprint grid
│   ├── BatchAdapter.java          — Tracker batch cards
│   └── ListingAdapter.java        — Marketplace listing cards
├── models/
│   ├── Design.java                — Design data model
│   ├── Batch.java                 — Batch tracking model
│   └── ProductListing.java        — Marketplace listing model
└── utils/
    ├── DataRepository.java        — 6 seeded bamboo designs
    └── PrefsHelper.java           — SharedPreferences persistence
```

---

## ✨ Key Features

- **Pinch-to-Zoom Blueprint** — Full matrix-based gesture zoom (1x–5x)
- **Price Suggester** — Material cost × labour + overhead % + profit % = fair price
- **Material Tracker** — Efficiency ratio (poles per unit), total cost tracking
- **Persistent Storage** — Batches and listings saved across app restarts (Gson + SharedPreferences)
- **Search & Filter** — Live search + category chip filtering on trends feed
- **Simulated Marketplace** — Mark as Sold, Delete, track by artisan name

---

## 🎨 Design System

| Token | Value |
|---|---|
| Primary Green | `#2D5016` |
| Secondary Amber | `#C8860A` |
| Accent Gold | `#E8C547` |
| Background | `#F8F4EE` |
| Font | Poppins (Regular / Medium / SemiBold / Bold) |
| Card Radius | 16dp |
| Button Radius | 12dp |

---

## 📦 Dependencies

```gradle
Material Components 1.11.0
ConstraintLayout 2.1.4
Navigation 2.7.6
RecyclerView 1.3.2
ViewPager2 1.0.0
Glide 4.16.0
Gson 2.10.1
Room 2.6.1 (ready for DB migration)
```

---

## 🌱 Impact

- **Artisanal Modernization** — 6 curated modern bamboo designs
- **Economic Growth** — Fair price calculator increases per-unit value
- **Sustainable Materials** — Promotes bamboo as green alternative

---

*Built for Hasta-Shilpa · GenAI Android Project · Western Ghats Artisans*
