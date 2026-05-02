package com.hastashilpa.app.utils;

import com.hastashilpa.app.models.Design;
import com.hastashilpa.app.models.Design.Dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataRepository {

    private static List<Design> cachedDesigns = null;

    /** Call this from a background thread during app startup to warm the cache. */
    public static void warmCache() {
        getAllDesigns();
    }

    public static List<Design> getAllDesigns() {
        if (cachedDesigns != null) return cachedDesigns;
        List<Design> designs = new ArrayList<>();

        // 1 - Bamboo Laptop Stand
        designs.add(new Design(1,
                "Bamboo Laptop Stand",
                "Office",
                "A sleek, ergonomic laptop stand crafted from sustainably sourced bamboo. Adjustable angle for perfect posture. Lightweight yet sturdy — holds up to 5 kg.",
                "bamboo_laptop_stand",
                "Medium",
                6,
                1800,
                "TRENDING",
                Arrays.asList("3 bamboo poles (3 cm dia)", "Cane webbing 0.5 m", "Sandpaper (180 grit)", "Linseed oil finish", "Jute cord 2 m"),
                Arrays.asList(
                        new Dimension("Overall Width", "32 cm"),
                        new Dimension("Overall Depth", "22 cm"),
                        new Dimension("Stand Height", "15 cm"),
                        new Dimension("Pole Diameter", "3 cm"),
                        new Dimension("Angle", "25°")
                ),
                Arrays.asList(
                        "Cut 4 bamboo poles to 32 cm for the base frame.",
                        "Cut 2 poles to 22 cm for side supports.",
                        "Drill 8 mm holes at 25° angle for leg joints.",
                        "Weave cane webbing between frame rails (0.5 m total).",
                        "Sand all surfaces with 180-grit sandpaper.",
                        "Apply 2 coats of linseed oil. Allow 4 hrs drying per coat.",
                        "Bind joints tightly with jute cord using figure-8 lashing.",
                        "Test stability with 3 kg weight before finishing."
                ),
                "3 poles", "0.5 m"
        ));

        // 2 - Modern Lamp Shade
        designs.add(new Design(2,
                "Woven Cane Lamp Shade",
                "Home Décor",
                "A boho-modern pendant lamp shade with tight hexagonal cane weave. Creates beautiful dappled light patterns. Suits living rooms and cafes alike.",
                "cane_lamp_shade",
                "Hard",
                10,
                2500,
                "TRENDING",
                Arrays.asList("Cane strips 4 m", "Bamboo ring frame 30 cm dia", "Cotton twine 1 m", "Varnish coat", "Pendant fitting (E27)"),
                Arrays.asList(
                        new Dimension("Diameter (Top)", "30 cm"),
                        new Dimension("Diameter (Bottom)", "40 cm"),
                        new Dimension("Height", "25 cm"),
                        new Dimension("Weave Gap", "8 mm"),
                        new Dimension("Frame Wire", "4 mm")
                ),
                Arrays.asList(
                        "Soak cane strips in water for 30 mins to increase flexibility.",
                        "Bend and bind bamboo into two rings: 30 cm (top) and 40 cm (bottom).",
                        "Connect rings with 6 vertical bamboo struts, equally spaced.",
                        "Begin hexagonal weaving from the top ring downward.",
                        "Secure each row with cotton twine at the vertical struts.",
                        "Complete bottom row and bind neatly to bottom ring.",
                        "Allow to dry fully (24 hrs) then apply one coat of varnish.",
                        "Attach pendant fitting to the top ring center."
                ),
                "1 pole (for rings)", "4 m"
        ));

        // 3 - Serving Tray
        designs.add(new Design(3,
                "Bamboo Serving Tray",
                "Kitchen",
                "An elegant rectangular serving tray with side handles. Food-safe finish. Ideal for breakfasts, restaurant plating, or gifting.",
                "bamboo_tray",
                "Easy",
                4,
                900,
                "NEW",
                Arrays.asList("2 bamboo poles (5 cm dia, flat split)", "Cane strips 1 m", "Food-safe lacquer", "Sandpaper 220 grit", "Small screws 8 pcs"),
                Arrays.asList(
                        new Dimension("Length", "45 cm"),
                        new Dimension("Width", "28 cm"),
                        new Dimension("Height (with handle)", "8 cm"),
                        new Dimension("Base Thickness", "1.5 cm"),
                        new Dimension("Handle Clearance", "4 cm")
                ),
                Arrays.asList(
                        "Split bamboo poles lengthwise to get flat panels.",
                        "Cut 3 flat panels to 45 cm × 28 cm and bind together.",
                        "Sand base panels with 220-grit until smooth.",
                        "Cut handle strips: 2 pieces 28 cm × 3 cm.",
                        "Attach handles at both short ends using screws + cane lashing.",
                        "Apply 3 coats of food-safe lacquer, sanding lightly between coats.",
                        "Final inspection: ensure no sharp edges remain."
                ),
                "2 poles", "1 m"
        ));

        // 4 - Plant Stand
        designs.add(new Design(4,
                "Tiered Plant Stand",
                "Garden",
                "A 3-tier bamboo plant stand perfect for balconies and living spaces. Holds pots up to 20 cm diameter. Stable tripod base design.",
                "bamboo_plant_stand",
                "Medium",
                8,
                1500,
                "NEW",
                Arrays.asList("6 bamboo poles (2.5 cm dia, 120 cm long)", "Cane lashing 3 m", "3 circular bamboo platforms 20 cm dia", "Sandpaper", "Outdoor varnish"),
                Arrays.asList(
                        new Dimension("Base Spread", "55 cm"),
                        new Dimension("Total Height", "110 cm"),
                        new Dimension("Tier 1 Height", "30 cm"),
                        new Dimension("Tier 2 Height", "65 cm"),
                        new Dimension("Tier 3 Height", "100 cm"),
                        new Dimension("Platform Dia", "20 cm")
                ),
                Arrays.asList(
                        "Bind 3 bamboo poles at top to form tripod frame using tight cane lashing.",
                        "Spread base evenly — 55 cm spread at ground level.",
                        "Attach 3 horizontal cross-rails at 30 cm, 65 cm, and 100 cm heights.",
                        "Place and secure circular bamboo platforms on each cross-rail.",
                        "Add diagonal brace poles for lateral stability.",
                        "Sand all surfaces. Apply 2 coats outdoor varnish.",
                        "Test with 2 kg pot before delivery."
                ),
                "6 poles", "3 m"
        ));

        // 5 - Wall Shelf
        designs.add(new Design(5,
                "Floating Bamboo Shelf",
                "Home Décor",
                "A minimal wall-mounted shelf with bamboo pole rails and a woven cane surface. Holds books, plants, or décor. Clean Japandi aesthetic.",
                "bamboo_shelf",
                "Easy",
                3,
                1200,
                "TRENDING",
                Arrays.asList("4 bamboo poles (2 cm dia, 60 cm long)", "Cane weave sheet 60×20 cm", "Wall brackets (4 pcs)", "Jute cord 1 m", "Sanding block"),
                Arrays.asList(
                        new Dimension("Length", "60 cm"),
                        new Dimension("Depth", "20 cm"),
                        new Dimension("Height (rail)", "10 cm"),
                        new Dimension("Wall Bracket Spacing", "40 cm"),
                        new Dimension("Load Capacity", "5 kg")
                ),
                Arrays.asList(
                        "Cut 2 bamboo rails to 60 cm and 2 side rails to 20 cm.",
                        "Join corners with mortise-and-tenon or cane lashing.",
                        "Weave cane sheet into the rectangular frame.",
                        "Attach wall brackets to the back rail at 40 cm spacing.",
                        "Sand smooth. Apply one coat of tung oil.",
                        "Mount on wall using provided brackets and M5 screws."
                ),
                "2 poles", "1.5 m"
        ));

        // 6 - Magazine Rack
        designs.add(new Design(6,
                "Cane Magazine Rack",
                "Office",
                "A sleek standing magazine and file rack. Open-weave cane sides allow ventilation. Fits A4 files perfectly. Office-ready design.",
                "cane_magazine_rack",
                "Medium",
                5,
                1100,
                "NEW",
                Arrays.asList("4 bamboo poles (3 cm dia, 40 cm)", "Cane strips 2 m", "Base board 30×15 cm", "Rubber feet 4 pcs", "Matte varnish"),
                Arrays.asList(
                        new Dimension("Width", "32 cm"),
                        new Dimension("Depth", "15 cm"),
                        new Dimension("Height", "38 cm"),
                        new Dimension("Slot Width", "3 cm"),
                        new Dimension("Base Thickness", "1 cm")
                ),
                Arrays.asList(
                        "Cut 4 vertical poles to 38 cm for corner posts.",
                        "Cut horizontal bamboo cross pieces to 32 cm (top/bottom).",
                        "Assemble rectangular frame and bind all joints with cane.",
                        "Weave open cane pattern on two long sides.",
                        "Attach base board (30×15 cm) to bottom frame.",
                        "Stick rubber feet to base corners to prevent scratching.",
                        "Apply matte varnish. Polish with soft cloth after drying."
                ),
                "4 poles", "2 m"
        ));

        cachedDesigns = designs;
        return designs;
    }

    public static List<Design> getFeaturedDesigns() {
        List<Design> all = getAllDesigns();
        List<Design> featured = new ArrayList<>();
        for (Design d : all) {
            if ("TRENDING".equals(d.getTag())) {
                featured.add(d);
            }
        }
        return featured;
    }

    public static List<Design> getDesignsByCategory(String category) {
        List<Design> all = getAllDesigns();
        if (category == null || category.equals("All")) return all;
        List<Design> filtered = new ArrayList<>();
        for (Design d : all) {
            if (d.getCategory().equals(category)) filtered.add(d);
        }
        return filtered;
    }

    public static Design getDesignById(int id) {
        for (Design d : getAllDesigns()) {
            if (d.getId() == id) return d;
        }
        return null;
    }
}
