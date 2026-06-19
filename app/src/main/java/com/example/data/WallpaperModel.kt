package com.example.data

import androidx.annotation.Keep

@Keep
data class Wallpaper(
    val id: String,
    val title: String,
    val category: String, // e.g., "AMOLED", "Nature", "Anime", "Space", "Animated", "Minimal"
    val imageUrl: String, // Unsplash or solid colors
    val isAnimated: Boolean = false,
    val isPremium: Boolean = false,
    val resolution: String = "4K Ultra HD",
    val author: String = "NexWall X AI Studio",
    val downloads: Int = 1200,
    val animatedType: String? = null // "matrix", "particles", "neon_glow", "liquid_flow"
)

object WallpaperData {
    val categories = listOf(
        "All",
        "Animated",
        "AMOLED",
        "Space",
        "Nature",
        "Anime",
        "Cyberpunk",
        "Minimal",
        "Gaming",
        "Football",
        "Cars"
    )

    private val curatedWallpapers = listOf(
        Wallpaper(
            id = "w1",
            title = "Cyberpunk Neo-Tokyo Street",
            category = "Cyberpunk",
            imageUrl = "https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&q=80&w=800",
            isAnimated = true,
            isPremium = false,
            animatedType = "neon_glow"
        ),
        Wallpaper(
            id = "w2",
            title = "Dark Void Spacewalk",
            category = "AMOLED",
            imageUrl = "https://images.unsplash.com/photo-1506318137071-a8e063b4bec0?auto=format&fit=crop&q=80&w=800",
            isPremium = true,
            resolution = "8K Premium OLED"
        ),
        Wallpaper(
            id = "w3",
            title = "Golden Hour Mountains Peak",
            category = "Nature",
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&q=80&w=800",
            isPremium = false
        ),
        Wallpaper(
            id = "w4",
            title = "Neon Abstract Particle Drift",
            category = "Animated",
            imageUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?auto=format&fit=crop&q=80&w=800",
            isAnimated = true,
            animatedType = "particles",
            resolution = "4K Live Wave"
        ),
        Wallpaper(
            id = "w5",
            title = "Retro Mech Anime Pilot",
            category = "Anime",
            imageUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?auto=format&fit=crop&q=80&w=800",
            isPremium = true
        ),
        Wallpaper(
            id = "w6",
            title = "Cosmic Matrix Code Stream",
            category = "Animated",
            imageUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?auto=format&fit=crop&q=80&w=800",
            isAnimated = true,
            animatedType = "matrix"
        ),
        Wallpaper(
            id = "w7",
            title = "Minimal Geometric Monolith",
            category = "Minimal",
            imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&q=80&w=800"
        ),
        Wallpaper(
            id = "w8",
            title = "Futuristic Hypercar",
            category = "Cars",
            imageUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&q=80&w=800",
            isPremium = true
        ),
        Wallpaper(
            id = "w9",
            title = "Nebula Supernova Spark",
            category = "Space",
            imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&q=80&w=800",
            isAnimated = true,
            animatedType = "particles"
        ),
        Wallpaper(
            id = "w10",
            title = "Legendary Jersey 10",
            category = "Football",
            imageUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&q=80&w=800",
            isPremium = false
        ),
        Wallpaper(
            id = "w11",
            title = "Misty Forest River",
            category = "Nature",
            imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&q=80&w=800",
            isPremium = false
        ),
        Wallpaper(
            id = "w12",
            title = "Synthwave Grid Valley",
            category = "Cyberpunk",
            imageUrl = "https://images.unsplash.com/photo-1515238152791-8216bfdf89a7?auto=format&fit=crop&q=80&w=800",
            isPremium = true
        ),
        Wallpaper(
            id = "w13",
            title = "Abyssal Deep AMOLED",
            category = "AMOLED",
            imageUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?auto=format&fit=crop&q=80&w=800",
            isPremium = true,
            resolution = "8K Premium OLED"
        ),
        Wallpaper(
            id = "w14",
            title = "Chibi Sakura Blossom Path",
            category = "Anime",
            imageUrl = "https://images.unsplash.com/photo-1528164344705-47542687000d?auto=format&fit=crop&q=80&w=800"
        ),
        Wallpaper(
            id = "w15",
            title = "Cyber Shard Cyberpunk Grid",
            category = "Gaming",
            imageUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format&fit=crop&q=80&w=800",
            isPremium = true
        ),
        Wallpaper(
            id = "w16",
            title = "Stellar Ring Galaxy",
            category = "Space",
            imageUrl = "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&q=80&w=800"
        ),
        Wallpaper(
            id = "w17",
            title = "Onyx Fluid Silk Ripple",
            category = "AMOLED",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&q=80&w=800",
            isAnimated = true,
            animatedType = "liquid_flow"
        ),
        Wallpaper(
            id = "w18",
            title = "Sunset Football Arena",
            category = "Football",
            imageUrl = "https://images.unsplash.com/photo-1575361204480-aadea25e6e68?auto=format&fit=crop&q=80&w=800",
            isPremium = true
        ),
        Wallpaper(
            id = "w19",
            title = "Matte Carbon Stealth Car",
            category = "Cars",
            imageUrl = "https://images.unsplash.com/photo-1525609004556-c46c7d6cf0a3?auto=format&fit=crop&q=80&w=800"
        ),
        Wallpaper(
            id = "w20",
            title = "Cosmic Storm Aurora",
            category = "Animated",
            imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&q=80&w=800",
            isAnimated = true,
            animatedType = "particles"
        )
    )

    val wallpapers: List<Wallpaper> = run {
        val list = mutableListOf<Wallpaper>()
        list.addAll(curatedWallpapers)

        val photoIdPool = listOf(
            "photo-1541701494587-cb58502866ab",
            "photo-1550684848-fac1c5b4e853",
            "photo-1579783900882-c0d3dad7b119",
            "photo-1518709268805-4e9042af9f23",
            "photo-1526374965328-7f61d4dc18c5",
            "photo-1506318137071-a8e063b4bec0",
            "photo-1618005182384-a83a8bd57fbe",
            "photo-1451187580459-43490279c0fa",
            "photo-1462331940025-496dfbfc7564",
            "photo-1502134249126-9f3755a50d78",
            "photo-1444703686981-a3abbc4d4fe3",
            "photo-1464822759023-fed622ff2c3b",
            "photo-1441974231531-c6227db76b6e",
            "photo-1470071459604-3b5ec3a7fe05",
            "photo-1513836279014-a89f7a76ae86",
            "photo-1578632767115-351597cf2477",
            "photo-1528164344705-47542687000d",
            "photo-1607604276583-eef5d076aa5f",
            "photo-1540959733332-eab4deceeaf7",
            "photo-1515238152791-8216bfdf89a7",
            "photo-1620641788421-7a1c342ea42e",
            "photo-1557683316-973673baf926",
            "photo-1604871000636-074fa5117945",
            "photo-1618005198143-e5283b519a7f",
            "photo-1542751371-adc38448a05e",
            "photo-1612287230202-1bf1d85d1bdf",
            "photo-1538481199705-c710c4e965fc",
            "photo-1508098682722-e99c43a406b2",
            "photo-1575361204480-aadea25e6e68",
            "photo-1551958219-acbc608c6d77",
            "photo-1503376780353-7e6692767b70",
            "photo-1525609004556-c46c7d6cf0a3",
            "photo-1552519507-da3b142c6e3d"
        )

        val subThemes = mapOf(
            "Animated" to listOf(
                "Liquid Neon Fusion", "Quantum Cyber Vortex", "Bionic Pulse Field", "Abyssal Particle Waves", 
                "Synthwave Grid Surge", "Hyperdrive Starlight", "Plasma Flare Loop", "Vibrant Hologram Shift", 
                "Matrix Code Flow", "Ethereal Neon Pulse", "Digital Aurora Wave"
            ),
            "AMOLED" to listOf(
                "Obsidian Void Eclipse", "Onyx Silk Geometry", "Midnight Cyber Ribbon", "Charcoal Matrix Depth", 
                "Pure Black Prism", "Ultra Matte Monolith", "Stealth Carbon Fiber", "Shadow Helix Shell", 
                "Abyssal Dark Aurora", "Prestige Black Velvet"
            ),
            "Space" to listOf(
                "Nebula Supernova Flare", "Andromeda Arm Cluster", "Stellar Cosmic Dust", "Jupiter Aurora Crown", 
                "Neutron Ring Collapse", "Interstellar Eclipse", "Pulsar Beam Core", "Milky Way Rift", 
                "Celestial Deep Prism", "Stardust Pillars"
            ),
            "Nature" to listOf(
                "Glacial Mountain Peak", "Verdant Autumn Forest", "Emerald River Delta", "Sunset Alpine Lake", 
                "Whispering Pine Meadow", "Ocean Storm Crest", "Volcanic Ridge Twilight", "Sienna Desert Dunes", 
                "Golden Hour Canopy", "Crystal Mist Canyon"
            ),
            "Anime" to listOf(
                "Neo Mech Command Center", "Retro Arcade Cyber Girl", "Sakura Petal Shinto Shrines", "Cyberpunk Samurai Katana", 
                "Sunset Train Station Rooftop", "Celestial Spirit Forest", "Magical Chibi Spellbook", "Enchanted Library Core", 
                "Starlight Anime Sky", "InkVoyage Samurai"
            ),
            "Cyberpunk" to listOf(
                "Holographic Cyber Alleyway", "Sector 7 Skyline", "Grid Neon Outpost", "Laser Arcade Lounge", 
                "Retro Hackspace Vault", "Chrome Titan Megastructures", "Augmented Neural Suite", "Cyber Junk Market", 
                "Android Assembly Lab", "Underground Subnet"
            ),
            "Minimal" to listOf(
                "Monolithic Slate Shadows", "Ethereal Silk Waves", "Prismatic Quartz Angle", "Linear Sunrise Horizon", 
                "Organic Sand Gradients", "Matte Block Sculpture", "Curved Pastel Ribbon", "Infinite Clay Ridge", 
                "Amoled Circle Array", "Brutalism Concrete Wedge"
            ),
            "Gaming" to listOf(
                "RGB Battle Station Core", "Cyber Grid Controller Glow", "Liquid Coolant Loop Apex", "Laser Esports Stage", 
                "Retro Console Mainframe", "Guild Hall Throne room", "Command Vessel Cockpit", "Dungeon Portal Gateway", 
                "Pixel Art Arcade Lobby", "Next Gen Graphics Blueprint"
            ),
            "Football" to listOf(
                "Stadium Floodlights Dawn", "Vibrant Grass Pitch Circle", "Golden Boot Showcase", "Championship Trophy Flare", 
                "Goal net Tension Arc", "Legendary Corner Arc", "Tactical Magnetic board", "Dynamic Match ball Twist", 
                "Team Crest Neon Glow", "Grandstand Crowd Blur"
            ),
            "Cars" to listOf(
                "Carbon Matte Aero Wing", "Drift Smoke Grand Prix", "Sleek Chrome Concept Spokes", "Twin Turbo Exhaust Flame", 
                "Mountain Ridge Sunset Cruise", "Vintage Racing Cockpit View", "Neon Highway Cruiser Rim", "Electric Core Coupe", 
                "Aero Concept Dynamic Grill", "Racetrack Start Underlights"
            )
        )

        val targetCategoryKeys = subThemes.keys.toList()
        var idCounter = 21

        // 1. Generate 420 STATIC wallpapers (with isAnimated = false)
        for (catIndex in 0 until 10) {
            val category = targetCategoryKeys[catIndex % targetCategoryKeys.size]
            val themes = subThemes[category] ?: listOf("Modern Wall")
            
            for (num in 1..42) {
                val themeName = themes[(num - 1) % themes.size]
                val title = "$themeName #${100 + num}"
                
                val poolIdx = when (category) {
                    "AMOLED" -> 0 + (num % 7)
                    "Space" -> 7 + (num % 4)
                    "Nature" -> 11 + (num % 4)
                    "Anime" -> 15 + (num % 3)
                    "Cyberpunk" -> 18 + (num % 3)
                    "Minimal" -> 21 + (num % 3)
                    "Gaming" -> 24 + (num % 3)
                    "Football" -> 27 + (num % 3)
                    "Cars" -> 30 + (num % 3)
                    else -> num % photoIdPool.size
                }
                val baseId = photoIdPool[poolIdx % photoIdPool.size]
                val urlWithUniqueSig = "https://images.unsplash.com/$baseId?auto=format&fit=crop&q=80&w=800&sig=${2000 + idCounter}"
                
                list.add(
                    Wallpaper(
                        id = "w$idCounter",
                        title = title,
                        category = category,
                        imageUrl = urlWithUniqueSig,
                        isAnimated = false,
                        isPremium = (idCounter % 3 == 0),
                        resolution = if (idCounter % 4 == 0) "8K Premium OLED" else "4K Ultra HD",
                        author = "NexWall Studio AI",
                        downloads = 1000 + (idCounter * 7) % 5000,
                        animatedType = null
                    )
                )
                idCounter++
            }
        }

        // 2. Generate 420 ANIMATED wallpapers (with isAnimated = true)
        for (catIndex in 0 until 10) {
            val category = targetCategoryKeys[catIndex % targetCategoryKeys.size]
            val themes = subThemes[category] ?: listOf("Live Wave")
            
            for (num in 1..42) {
                val themeName = themes[(num - 1) % themes.size]
                val title = "$themeName Live #${500 + num}"
                
                val poolIdx = when (category) {
                    "AMOLED" -> 0 + (num % 7)
                    "Space" -> 7 + (num % 4)
                    "Nature" -> 11 + (num % 4)
                    "Anime" -> 15 + (num % 3)
                    "Cyberpunk" -> 18 + (num % 3)
                    "Minimal" -> 21 + (num % 3)
                    "Gaming" -> 24 + (num % 3)
                    "Football" -> 27 + (num % 3)
                    "Cars" -> 30 + (num % 3)
                    else -> num % photoIdPool.size
                }
                val baseId = photoIdPool[poolIdx % photoIdPool.size]
                val urlWithUniqueSig = "https://images.unsplash.com/$baseId?auto=format&fit=crop&q=80&w=800&sig=${5000 + idCounter}"
                
                list.add(
                    Wallpaper(
                        id = "w$idCounter",
                        title = title,
                        category = "Animated", // Also place them in Animated category directly so the Live chip list is huge and diverse
                        imageUrl = urlWithUniqueSig,
                        isAnimated = true,
                        isPremium = (idCounter % 3 == 0),
                        resolution = if (idCounter % 4 == 0) "8K Premium OLED" else "4K Ultra HD",
                        author = "NexWall Studio Live Engine",
                        downloads = 1500 + (idCounter * 9) % 8000,
                        animatedType = when (num % 4) {
                            0 -> "neon_glow"
                            1 -> "matrix"
                            2 -> "particles"
                            else -> "liquid_flow"
                        }
                    )
                )
                idCounter++
            }
        }
        list
    }
}
