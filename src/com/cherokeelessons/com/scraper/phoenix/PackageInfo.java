
/*
 * EXPORT AS RUNNABLE JAR TO: E:\WebScrapers\SEC_Filings\scraper-sec.jar
 */

package com.cherokeelessons.com.scraper.phoenix;

public class PackageInfo {

}

/*

===
From: Alan Henderson [mailto:adhender@bellsouth.net]
Sent: Monday, August 13, 2012 11:43 AM

Title Template:
Form 10-Q for VISTA INTERNATIONAL TECHNOLOGIES INC

===
From: Alan Henderson [mailto:adhender@bellsouth.net]
Sent: Wednesday, August 08, 2012 1:40 PM
Abstract Template:
Sample Template:

By a News Reporter-Staff News Editor at [PUBTITLE] -- According to news reporting originating from Washington, D.C., by [OUR COMPANY NAME HERE] journalists, a U.S. Securities and Exchange Commission (SEC) filing by [COMPANY] was cleared and accepted on [ACCEPTEDDATE].
Form [FORMTYPE] ([BRIEF DESCRIPTION]) was submitted on [FILINGDATE].
For additional information on this SEC filing see: [URL].
A U.S. Securities and Exchange Commission filing is a formal document or financial statement  submitted to the SEC by publicly-traded companies.
Form [FORMTYPE] is used to report the occurrence of any material events or corporate changes which are of importance to investors or security holders and previously have not been reported by the registrant.

Sample Article:

By a News Reporter-Staff News Editor at Investment Weekly News -- According to news reporting originating from Washington, D.C., by VerticalNews journalists, a U.S. Securities and Exchange Commission (SEC) filing by NewsRx was cleared and accepted on August 8, 2012.
Form 8-K (Initial statement of beneficial ownership of securities) was submitted on August 8, 2012.
For additional information on this SEC filing see: [URL].
An SEC filing is a financial statement or other formal document submitted to the SEC by publicly-traded companies.
Form 8-K is used to report the occurrence of any material events or corporate changes which are of importance to investors or security holders and previously have not been reported by the registrant.****

===
From: Alan Henderson [mailto:adhender@bellsouth.net]
Sent: Friday, August 10, 2012 12:11 PM
To: 'Chantay Jones, NewsRx'
Subject: File Posted

Chantay:

I have posted forms_with_descriptions.zip.
The included table has two fields: FORM and DESCRIPT.
This is the table that needs to be used to create the paragraph below.
Form [FORMTYPE] is used to report the occurrence of any material events or corporate changes which are of importance to investors or security holders and previously have not been reported by the registrant
I would change the template design for the above paragraph as follows:

Form [FORMTYPE]: [DESCRIPT].

Example:
Form 8-K:  This form is used to report the occurrence of any material events or corporate changes which are of importance to investors or security holders and previously have not been reported by the registrant.

Please let me know if you need additional clarification.

Alan

*/


/*
 * 

http://www.sec.gov/info/edgar/siccodes.htm

SIC
Code 	A/D  
Office 	  	Industry Title
100 	5 	  	AGRICULTURAL PRODUCTION-CROPS
200 	5 	  	AGRICULTURAL PROD-LIVESTOCK & ANIMAL SPECIALTIES
700 	5 	  	AGRICULTURAL SERVICES
800 	5 	  	FORESTRY
900 	5 	  	FISHING, HUNTING AND TRAPPING
1000 	9 	  	METAL MINING
1040 	9 	  	GOLD AND SILVER ORES
1090 	9 	  	MISCELLANEOUS METAL ORES
1220 	9 	  	BITUMINOUS COAL & LIGNITE MINING
1221 	9 	  	BITUMINOUS COAL & LIGNITE SURFACE MINING
1311 	4 	  	CRUDE PETROLEUM & NATURAL GAS
1381 	4 	  	DRILLING OIL & GAS WELLS
1382 	4 	  	OIL & GAS FIELD EXPLORATION SERVICES
1389 	4 	  	OIL & GAS FIELD SERVICES, NEC
1400 	9 	  	MINING & QUARRYING OF NONMETALLIC MINERALS (NO FUELS)
1520 	6 	  	GENERAL BLDG CONTRACTORS - RESIDENTIAL BLDGS
1531 	6 	  	OPERATIVE BUILDERS
1540 	6 	  	GENERAL BLDG CONTRACTORS - NONRESIDENTIAL BLDGS
1600 	6 	  	HEAVY CONSTRUCTION OTHER THAN BLDG CONST - CONTRACTORS
1623 	6 	  	WATER, SEWER, PIPELINE, COMM & POWER LINE CONSTRUCTION
1700 	6 	  	CONSTRUCTION - SPECIAL TRADE CONTRACTORS
1731 	6 	  	ELECTRICAL WORK
2000 	4 	  	FOOD AND KINDRED PRODUCTS
2011 	5 	  	MEAT PACKING PLANTS
2013 	5 	  	SAUSAGES & OTHER PREPARED MEAT PRODUCTS
2015 	5 	  	POULTRY SLAUGHTERING AND PROCESSING
2020 	4 	  	DAIRY PRODUCTS
2024 	4 	  	ICE CREAM & FROZEN DESSERTS
2030 	4 	  	CANNED, FROZEN & PRESERVD FRUIT, VEG & FOOD SPECIALTIES
2033 	4 	  	CANNED, FRUITS, VEG, PRESERVES, JAMS & JELLIES
2040 	4 	  	GRAIN MILL PRODUCTS
2050 	4 	  	BAKERY PRODUCTS
2052 	4 	  	COOKIES & CRACKERS
2060 	4 	  	SUGAR & CONFECTIONERY PRODUCTS
2070 	4 	  	FATS & OILS
2080 	9 	  	BEVERAGES
2082 	9 	  	MALT BEVERAGES
2086 	9 	  	BOTTLED & CANNED SOFT DRINKS & CARBONATED WATERS
2090 	4 	  	MISCELLANEOUS FOOD PREPARATIONS & KINDRED PRODUCTS
2092 	4 	  	PREPARED FRESH OR FROZEN FISH & SEAFOODS
2100 	5 	  	TOBACCO PRODUCTS
2111 	5 	  	CIGARETTES
2200 	2 	  	TEXTILE MILL PRODUCTS
2211 	2 	  	BROADWOVEN FABRIC MILLS, COTTON
2221 	2 	  	BROADWOVEN FABRIC MILLS, MAN MADE FIBER & SILK
2250 	2 	  	KNITTING MILLS
2253 	9 	  	KNIT OUTERWEAR MILLS
2273 	2 	  	CARPETS & RUGS
2300 	9 	  	APPAREL & OTHER FINISHD PRODS OF FABRICS & SIMILAR MATL
2320 	9 	  	MEN'S & BOYS' FURNISHGS, WORK CLOTHG, & ALLIED GARMENTS
2330 	9 	  	WOMEN'S, MISSES', AND JUNIORS OUTERWEAR
2340 	9 	  	WOMEN'S, MISSES', CHILDREN'S & INFANTS' UNDERGARMENTS
2390 	9 	  	MISCELLANEOUS FABRICATED TEXTILE PRODUCTS
2400 	6 	  	LUMBER & WOOD PRODUCTS (NO FURNITURE)
2421 	6 	  	SAWMILLS & PLANTING MILLS, GENERAL
2430 	6 	  	MILLWOOD, VENEER, PLYWOOD, & STRUCTURAL WOOD MEMBERS
2451 	6 	  	MOBILE HOMES
2452 	6 	  	PREFABRICATED WOOD BLDGS & COMPONENTS
2510 	6 	  	HOUSEHOLD FURNITURE
2511 	6 	  	WOOD HOUSEHOLD FURNITURE, (NO UPHOLSTERED)
2520 	6 	  	OFFICE FURNITURE
2522 	6 	  	OFFICE FURNITURE (NO WOOD)
2531 	6 	  	PUBLIC BLDG & RELATED FURNITURE
2540 	6 	  	PARTITIONS, SHELVG, LOCKERS, & OFFICE & STORE FIXTURES
2590 	6 	  	MISCELLANEOUS FURNITURE & FIXTURES
2600 	4 	  	PAPERS & ALLIED PRODUCTS
2611 	4 	  	PULP MILLS
2621 	4 	  	PAPER MILLS
2631 	4 	  	PAPERBOARD MILLS
2650 	4 	  	PAPERBOARD CONTAINERS & BOXES
2670 	4 	  	CONVERTED PAPER & PAPERBOARD PRODS (NO CONTANERS/BOXES)
2673 	6 	  	PLASTICS, FOIL & COATED PAPER BAGS
2711 	5 	  	NEWSPAPERS: PUBLISHING OR PUBLISHING & PRINTING
2721 	5 	  	PERIODICALS: PUBLISHING OR PUBLISHING & PRINTING
2731 	5 	  	BOOKS: PUBLISHING OR PUBLISHING & PRINTING
2732 	5 	  	BOOK PRINTING
2741 	5 	  	MISCELLANEOUS PUBLISHING
2750 	5 	  	COMMERCIAL PRINTING
2761 	5 	  	MANIFOLD BUSINESS FORMS
2771 	5 	  	GREETING CARDS
2780 	5 	  	BLANKBOOKS, LOOSELEAF BINDERS & BOOKBINDG & RELATD WORK
2790 	5 	  	SERVICE INDUSTRIES FOR THE PRINTING TRADE
2800 	6 	  	CHEMICALS & ALLIED PRODUCTS
2810 	6 	  	INDUSTRIAL INORGANIC CHEMICALS
2820 	6 	  	PLASTIC MATERIAL, SYNTH RESIN/RUBBER, CELLULOS (NO GLASS)
2821 	6 	  	PLASTIC MATERIALS, SYNTH RESINS & NONVULCAN ELASTOMERS
2833 	1 	  	MEDICINAL CHEMICALS & BOTANICAL PRODUCTS
2834 	1 	  	PHARMACEUTICAL PREPARATIONS
2835 	1 	  	IN VITRO & IN VIVO DIAGNOSTIC SUBSTANCES
2836 	1 	  	BIOLOGICAL PRODUCTS, (NO DISGNOSTIC SUBSTANCES)
2840 	6 	  	SOAP, DETERGENTS, CLEANG PREPARATIONS, PERFUMES, COSMETICS
2842 	6 	  	SPECIALTY CLEANING, POLISHING AND SANITATION PREPARATIONS
2844 	6 	  	PERFUMES, COSMETICS & OTHER TOILET PREPARATIONS
2851 	6 	  	PAINTS, VARNISHES, LACQUERS, ENAMELS & ALLIED PRODS
2860 	6 	  	INDUSTRIAL ORGANIC CHEMICALS
2870 	5 	  	AGRICULTURAL CHEMICALS
2890 	6 	  	MISCELLANEOUS CHEMICAL PRODUCTS
2891 	6 	  	ADHESIVES & SEALANTS
2911 	4 	  	PETROLEUM REFINING
2950 	6 	  	ASPHALT PAVING & ROOFING MATERIALS
2990 	6 	  	MISCELLANEOUS PRODUCTS OF PETROLEUM & COAL
3011 	6 	  	TIRES & INNER TUBES
3021 	6 	  	RUBBER & PLASTICS FOOTWEAR
3050 	6 	  	GASKETS, PACKG & SEALG DEVICES & RUBBER & PLASTICS HOSE
3060 	6 	  	FABRICATED RUBBER PRODUCTS, NEC
3080 	6 	  	MISCELLANEOUS PLASTICS PRODUCTS
3081 	6 	  	UNSUPPORTED PLASTICS FILM & SHEET
3086 	6 	  	PLASTICS FOAM PRODUCTS
3089 	6 	  	PLASTICS PRODUCTS, NEC
3100 	9 	  	LEATHER & LEATHER PRODUCTS
3140 	9 	  	FOOTWEAR, (NO RUBBER)
3211 	6 	  	FLAT GLASS
3220 	6 	  	GLASS & GLASSWARE, PRESSED OR BLOWN
3221 	6 	  	GLASS CONTAINERS
3231 	6 	  	GLASS PRODUCTS, MADE OF PURCHASED GLASS
3241 	6 	  	CEMENT, HYDRAULIC
3250 	6 	  	STRUCTURAL CLAY PRODUCTS
3260 	6 	  	POTTERY & RELATED PRODUCTS
3270 	6 	  	CONCRETE, GYPSUM & PLASTER PRODUCTS
3272 	6 	  	CONCRETE PRODUCTS, EXCEPT BLOCK & BRICK
3281 	6 	  	CUT STONE & STONE PRODUCTS
3290 	6 	  	ABRASIVE, ASBESTOS & MISC NONMETALLIC MINERAL PRODS
3310 	6 	  	STEEL WORKS, BLAST FURNACES & ROLLING & FINISHING MILLS
3312 	6 	  	STEEL WORKS, BLAST FURNACES & ROLLING MILLS (COKE OVENS)
3317 	6 	  	STEEL PIPE & TUBES
3320 	6 	  	IRON & STEEL FOUNDRIES
3330 	9 	  	PRIMARY SMELTING & REFINING OF NONFERROUS METALS
3334 	9 	  	PRIMARY PRODUCTION OF ALUMINUM
3341 	6 	  	SECONDARY SMELTING & REFINING OF NONFERROUS METALS
3350 	6 	  	ROLLING DRAWING & EXTRUDING OF NONFERROUS METALS
3357 	6 	  	DRAWING & INSULATING OF NONFERROUS WIRE
3360 	6 	  	NONFERROUS FOUNDRIES (CASTINGS)
3390 	6 	  	MISCELLANEOUS PRIMARY METAL PRODUCTS
3411 	6 	  	METAL CANS
3412 	6 	  	METAL SHIPPING BARRELS, DRUMS, KEGS & PAILS
3420 	6 	  	CUTLERY, HANDTOOLS & GENERAL HARDWARE
3430 	6 	  	HEARING EQUIP, EXCEPT ELEC & WARM AIR; & PLUMBING FIXTURES
3433 	6 	  	HEATING EQUIPMENT, EXCEPT ELECTRIC & WARM AIR FURNACES
3440 	6 	  	FABRICATED STRUCTURAL METAL PRODUCTS
3442 	6 	  	METAL DOORS, SASH, FRAMES, MOLDINGS & TRIM
3443 	6 	  	FABRICATED PLATE WORK (BOILER SHOPS)
3444 	6 	  	SHEET METAL WORK
3448 	6 	  	PREFABRICATED METAL BUILDINGS & COMPONENTS
3451 	6 	  	SCREW MACHINE PRODUCTS
3452 	6 	  	BOLTS, NUTS, SCREWS, RIVETS & WASHERS
3460 	6 	  	METAL FORGINGS & STAMPINGS
3470 	6 	  	COATING, ENGRAVING & ALLIED SERVICES
3480 	6 	  	ORDNANCE & ACCESSORIES, (NO VEHICLES/GUIDED MISSILES)
3490 	6 	  	MISCELLANEOUS FABRICATED METAL PRODUCTS
3510 	10 	  	ENGINES & TURBINES
3523 	10 	  	FARM MACHINERY & EQUIPMENT
3524 	10 	  	LAWN & GARDEN TRACTORS & HOME LAWN & GARDENS EQUIP
3530 	10 	  	CONSTRUCTION, MINING & MATERIALS HANDLING MACHINERY & EQUIP
3531 	10 	  	CONSTRUCTION MACHINERY & EQUIP
3532 	10 	  	MINING MACHINERY & EQUIP (NO OIL & GAS FIELD MACH & EQUIP)
3533 	4 	  	OIL & GAS FIELD MACHINERY & EQUIPMENT
3537 	10 	  	INDUSTRIAL TRUCKS, TRACTORS, TRAILORS & STACKERS
3540 	10 	  	METALWORKG MACHINERY & EQUIPMENT
3541 	10 	  	MACHINE TOOLS, METAL CUTTING TYPES
3550 	10 	  	SPECIAL INDUSTRY MACHINERY (NO METALWORKING MACHINERY)
3555 	10 	  	PRINTING TRADES MACHINERY & EQUIPMENT
3559 	10 	  	SPECIAL INDUSTRY MACHINERY, NEC
3560 	10 	  	GENERAL INDUSTRIAL MACHINERY & EQUIPMENT
3561 	10 	  	PUMPS & PUMPING EQUIPMENT
3562 	6 	  	BALL & ROLLER BEARINGS
3564 	6 	  	INDUSTRIAL & COMMERCIAL FANS & BLOWERS & AIR PURIFING EQUIP
3567 	6 	  	INDUSTRIAL PROCESS FURNACES & OVENS
3569 	6 	  	GENERAL INDUSTRIAL MACHINERY & EQUIPMENT, NEC
3570 	3 	  	COMPUTER & OFFICE EQUIPMENT
3571 	3 	  	ELECTRONIC COMPUTERS
3572 	3 	  	COMPUTER STORAGE DEVICES
3575 	3 	  	COMPUTER TERMINALS
3576 	3 	  	COMPUTER COMMUNICATIONS EQUIPMENT
3577 	3 	  	COMPUTER PERIPHERAL EQUIPMENT, NEC
3578 	3 	  	CALCULATING & ACCOUNTING MACHINES (NO ELECTRONIC COMPUTERS)
3579 	3 	  	OFFICE MACHINES, NEC
3580 	6 	  	REFRIGERATION & SERVICE INDUSTRY MACHINERY
3585 	6 	  	AIR-COND & WARM AIR HEATG EQUIP & COMM & INDL REFRIG EQUIP
3590 	6 	  	MISC INDUSTRIAL & COMMERCIAL MACHINERY & EQUIPMENT
3600 	10 	  	ELECTRONIC & OTHER ELECTRICAL EQUIPMENT (NO COMPUTER EQUIP)
3612 	10 	  	POWER, DISTRIBUTION & SPECIALTY TRANSFORMERS
3613 	10 	  	SWITCHGEAR & SWITCHBOARD APPARATUS
3620 	10 	  	ELECTRICAL INDUSTRIAL APPARATUS
3621 	10 	  	MOTORS & GENERATORS
3630 	11 	  	HOUSEHOLD APPLIANCES
3634 	11 	  	ELECTRIC HOUSEWARES & FANS
3640 	11 	  	ELECTRIC LIGHTING & WIRING EQUIPMENT
3651 	11 	  	HOUSEHOLD AUDIO & VIDEO EQUIPMENT
3652 	11 	  	PHONOGRAPH RECORDS & PRERECORDED AUDIO TAPES & DISKS
3661 	11 	  	TELEPHONE & TELEGRAPH APPARATUS
3663 	11 	  	RADIO & TV BROADCASTING & COMMUNICATIONS EQUIPMENT
3669 	11 	  	COMMUNICATIONS EQUIPMENT, NEC
3670 	10 	  	ELECTRONIC COMPONENTS & ACCESSORIES
3672 	3 	  	PRINTED CIRCUIT BOARDS
3674 	10 	  	SEMICONDUCTORS & RELATED DEVICES
3677 	10 	  	ELECTRONIC COILS, TRANSFORMERS & OTHER INDUCTORS
3678 	10 	  	ELECTRONIC CONNECTORS
3679 	10 	  	ELECTRONIC COMPONENTS, NEC
3690 	10 	  	MISCELLANEOUS ELECTRICAL MACHINERY, EQUIPMENT & SUPPLIES
3695 	11 	  	MAGNETIC & OPTICAL RECORDING MEDIA
3711 	5 	  	MOTOR VEHICLES & PASSENGER CAR BODIES
3713 	5 	  	TRUCK & BUS BODIES
3714 	5 	  	MOTOR VEHICLE PARTS & ACCESSORIES
3715 	5 	  	TRUCK TRAILERS
3716 	5 	  	MOTOR HOMES
3720 	5 	  	AIRCRAFT & PARTS
3721 	5 	  	AIRCRAFT
3724 	5 	  	AIRCRAFT ENGINES & ENGINE PARTS
3728 	5 	  	AIRCRAFT PARTS & AUXILIARY EQUIPMENT, NEC
3730 	5 	  	SHIP & BOAT BUILDING & REPAIRING
3743 	5 	  	RAILROAD EQUIPMENT
3751 	5 	  	MOTORCYCLES, BICYCLES & PARTS
3760 	5 	  	GUIDED MISSILES & SPACE VEHICLES & PARTS
3790 	5 	  	MISCELLANEOUS TRANSPORTATION EQUIPMENT
3812 	5 	  	SEARCH, DETECTION, NAVAGATION, GUIDANCE, AERONAUTICAL SYS
3821 	10 	  	LABORATORY APPARATUS & FURNITURE
3822 	10 	  	AUTO CONTROLS FOR REGULATING RESIDENTIAL & COMML ENVIRONMENTS
3823 	10 	  	INDUSTRIAL INSTRUMENTS FOR MEASUREMENT, DISPLAY, AND CONTROL
3824 	10 	  	TOTALIZING FLUID METERS & COUNTING DEVICES
3825 	10 	  	INSTRUMENTS FOR MEAS & TESTING OF ELECTRICITY & ELEC SIGNALS
3826 	10 	  	LABORATORY ANALYTICAL INSTRUMENTS
3827 	10 	  	OPTICAL INSTRUMENTS & LENSES
3829 	10 	  	MEASURING & CONTROLLING DEVICES, NEC
3841 	10 	  	SURGICAL & MEDICAL INSTRUMENTS & APPARATUS
3842 	10 	  	ORTHOPEDIC, PROSTHETIC & SURGICAL APPLIANCES & SUPPLIES
3843 	10 	  	DENTAL EQUIPMENT & SUPPLIES
3844 	10 	  	X-RAY APPARATUS & TUBES & RELATED IRRADIATION APPARATUS
3845 	10 	  	ELECTROMEDICAL & ELECTROTHERAPEUTIC APPARATUS
3851 	10 	  	OPHTHALMIC GOODS
3861 	10 	  	PHOTOGRAPHIC EQUIPMENT & SUPPLIES
3873 	2 	  	WATCHES, CLOCKS, CLOCKWORK OPERATED DEVICES/PARTS
3910 	2 	  	JEWELRY, SILVERWARE & PLATED WARE
3911 	2 	  	JEWELRY, PRECIOUS METAL
3931 	5 	  	MUSICAL INSTRUMENTS
3942 	5 	  	DOLLS & STUFFED TOYS
3944 	5 	  	GAMES, TOYS & CHILDREN'S VEHICLES (NO DOLLS & BICYCLES)
3949 	5 	  	SPORTING & ATHLETIC GOODS, NEC
3950 	9 	  	PENS, PENCILS & OTHER ARTISTS' MATERIALS
3960 	6 	  	COSTUME JEWELRY & NOVELTIES
3990 	6 	  	MISCELLANEOUS MANUFACTURING INDUSTRIES
4011 	5 	  	RAILROADS, LINE-HAUL OPERATING
4013 	5 	  	RAILROAD SWITCHING & TERMINAL ESTABLISHMENTS
4100 	5 	  	LOCAL & SUBURBAN TRANSIT & INTERURBAN HWY PASSENGER TRANS
4210 	5 	  	TRUCKING & COURIER SERVICES (NO AIR)
4213 	5 	  	TRUCKING (NO LOCAL)
4220 	5 	  	PUBLIC WAREHOUSING & STORAGE
4231 	5 	  	TERMINAL MAINTENANCE FACILITIES FOR MOTOR FREIGHT TRANSPORT
4400 	5 	  	WATER TRANSPORTATION
4412 	5 	  	DEEP SEA FOREIGN TRANSPORTATION OF FREIGHT
4512 	5 	  	AIR TRANSPORTATION, SCHEDULED
4513 	5 	  	AIR COURIER SERVICES
4522 	5 	  	AIR TRANSPORTATION, NONSCHEDULED
4581 	5 	  	AIRPORTS, FLYING FIELDS & AIRPORT TERMINAL SERVICES
4610 	4 	  	PIPE LINES (NO NATURAL GAS)
4700 	5 	  	TRANSPORTATION SERVICES
4731 	5 	  	ARRANGEMENT OF TRANSPORTATION OF FREIGHT & CARGO
4812 	11 	  	RADIOTELEPHONE COMMUNICATIONS
4813 	11 	  	TELEPHONE COMMUNICATIONS (NO RADIOTELEPHONE)
4822 	11 	  	TELEGRAPH & OTHER MESSAGE COMMUNICATIONS
4832 	11 	  	RADIO BROADCASTING STATIONS
4833 	11 	  	TELEVISION BROADCASTING STATIONS
4841 	11 	  	CABLE & OTHER PAY TELEVISION SERVICES
4899 	11 	  	COMMUNICATIONS SERVICES, NEC
4900 	2 	  	ELECTRIC, GAS & SANITARY SERVICES
4911 	2 	  	ELECTRIC SERVICES
4922 	2 	  	NATURAL GAS TRANSMISSION
4923 	2 	  	NATURAL GAS TRANSMISISON & DISTRIBUTION
4924 	2 	  	NATURAL GAS DISTRIBUTION
4931 	2 	  	ELECTRIC & OTHER SERVICES COMBINED
4932 	2 	  	GAS & OTHER SERVICES COMBINED
4941 	2 	  	WATER SUPPLY
4950 	6 	  	SANITARY SERVICES
4953 	6 	  	REFUSE SYSTEMS
4955 	6 	  	HAZARDOUS WASTE MANAGEMENT
4961 	2 	  	STEAM & AIR-CONDITIONING SUPPLY
4991 	2 	  	COGENERATION SERVICES & SMALL POWER PRODUCERS
5000 	2 	  	WHOLESALE-DURABLE GOODS
5010 	5 	  	WHOLESALE-MOTOR VEHICLES & MOTOR VEHICLE PARTS & SUPPLIES
5013 	5 	  	WHOLESALE-MOTOR VEHICLE SUPPLIES & NEW PARTS
5020 	2 	  	WHOLESALE-FURNITURE & HOME FURNISHINGS
5030 	6 	  	WHOLESALE-LUMBER & OTHER CONSTRUCTION MATERIALS
5031 	6 	  	WHOLESALE-LUMBER, PLYWOOD, MILLWORK & WOOD PANELS
5040 	2 	  	WHOLESALE-PROFESSIONAL & COMMERCIAL EQUIPMENT & SUPPLIES
5045 	3 	  	WHOLESALE-COMPUTERS & PERIPHERAL EQUIPMENT & SOFTWARE
5047 	9 	  	WHOLESALE-MEDICAL, DENTAL & HOSPITAL EQUIPMENT & SUPPLIES
5050 	5 	  	WHOLESALE-METALS & MINERALS (NO PETROLEUM)
5051 	5 	  	WHOLESALE-METALS SERVICE CENTERS & OFFICES
5063 	10 	  	WHOLESALE-ELECTRICAL APPARATUS & EQUIPMENT, WIRING SUPPLIES
5064 	10 	  	WHOLESALE-ELECTRICAL APPLIANCES, TV & RADIO SETS
5065 	10 	  	WHOLESALE-ELECTRONIC PARTS & EQUIPMENT, NEC
5070 	6 	  	WHOLESALE-HARDWARE & PLUMBING & HEATING EQUIPMENT & SUPPLIES
5072 	6 	  	WHOLESALE-HARDWARE
5080 	6 	  	WHOLESALE-MACHINERY, EQUIPMENT & SUPPLIES
5082 	6 	  	WHOLESALE-CONSTRUCTION & MINING (NO PETRO) MACHINERY & EQUIP
5084 	6 	  	WHOLESALE-INDUSTRIAL MACHINERY & EQUIPMENT
5090 	2 	  	WHOLESALE-MISC DURABLE GOODS
5094 	2 	  	WHOLESALE-JEWELRY, WATCHES, PRECIOUS STONES & METALS
5099 	2 	  	WHOLESALE-DURABLE GOODS, NEC
5110 	4 	  	WHOLESALE-PAPER & PAPER PRODUCTS
5122 	9 	  	WHOLESALE-DRUGS, PROPRIETARIES & DRUGGISTS' SUNDRIES
5130 	9 	  	WHOLESALE-APPAREL, PIECE GOODS & NOTIONS
5140 	2 	  	WHOLESALE-GROCERIES & RELATED PRODUCTS
5141 	2 	  	WHOLESALE-GROCERIES, GENERAL LINE
5150 	5 	  	WHOLESALE-FARM PRODUCT RAW MATERIALS
5160 	6 	  	WHOLESALE-CHEMICALS & ALLIED PRODUCTS
5171 	4 	  	WHOLESALE-PETROLEUM BULK STATIONS & TERMINALS
5172 	4 	  	WHOLESALE-PETROLEUM & PETROLEUM PRODUCTS (NO BULK STATIONS)
5180 	9 	  	WHOLESALE-BEER, WINE & DISTILLED ALCOHOLIC BEVERAGES
5190 	2 	  	WHOLESALE-MISCELLANEOUS NONDURABLE GOODS
5200 	6 	  	RETAIL-BUILDING MATERIALS, HARDWARE, GARDEN SUPPLY
5211 	6 	  	RETAIL-LUMBER & OTHER BUILDING MATERIALS DEALERS
5271 	2 	  	RETAIL-MOBILE HOME DEALERS
5311 	2 	  	RETAIL-DEPARTMENT STORES
5331 	2 	  	RETAIL-VARIETY STORES
5399 	2 	  	RETAIL-MISC GENERAL MERCHANDISE STORES
5400 	2 	  	RETAIL-FOOD STORES
5411 	2 	  	RETAIL-GROCERY STORES
5412 	2 	  	RETAIL-CONVENIENCE STORES
5500 	2 	  	RETAIL-AUTO DEALERS & GASOLINE STATIONS
5531 	2 	  	RETAIL-AUTO & HOME SUPPLY STORES
5600 	9 	  	RETAIL-APPAREL & ACCESSORY STORES
5621 	9 	  	RETAIL-WOMEN'S CLOTHING STORES
5651 	9 	  	RETAIL-FAMILY CLOTHING STORES
5661 	9 	  	RETAIL-SHOE STORES
5700 	2 	  	RETAIL-HOME FURNITURE, FURNISHINGS & EQUIPMENT STORES
5712 	2 	  	RETAIL-FURNITURE STORES
5731 	2 	  	RETAIL-RADIO, TV & CONSUMER ELECTRONICS STORES
5734 	2 	  	RETAIL-COMPUTER & COMPUTER SOFTWARE STORES
5735 	2 	  	RETAIL-RECORD & PRERECORDED TAPE STORES
5810 	5 	  	RETAIL-EATING & DRINKING PLACES
5812 	5 	  	RETAIL-EATING PLACES
5900 	2 	  	RETAIL-MISCELLANEOUS RETAIL
5912 	1 	  	RETAIL-DRUG STORES AND PROPRIETARY STORES
5940 	2 	  	RETAIL-MISCELLANEOUS SHOPPING GOODS STORES
5944 	2 	  	RETAIL-JEWELRY STORES
5945 	2 	  	RETAIL-HOBBY, TOY & GAME SHOPS
5960 	2 	  	RETAIL-NONSTORE RETAILERS
5961 	2 	  	RETAIL-CATALOG & MAIL-ORDER HOUSES
5990 	2 	  	RETAIL-RETAIL STORES, NEC
6021 	7 	  	NATIONAL COMMERCIAL BANKS
6022 	7 	  	STATE COMMERCIAL BANKS
6029 	7 & 12 	  	COMMERCIAL BANKS, NEC
6035 	7 	  	SAVINGS INSTITUTION, FEDERALLY CHARTERED
6036 	7 	  	SAVINGS INSTITUTIONS, NOT FEDERALLY CHARTERED
6099 	7 	  	FUNCTIONS RELATED TO DEPOSITORY BANKING, NEC
6111 	12 	  	FEDERAL & FEDERALLY-SPONSORED CREDIT AGENCIES
6141 	7 	  	PERSONAL CREDIT INSTITUTIONS
6153 	7 	  	SHORT-TERM BUSINESS CREDIT INSTITUTIONS
6159 	7 	  	MISCELLANEOUS BUSINESS CREDIT INSTITUTION
6162 	7 	  	MORTGAGE BANKERS & LOAN CORRESPONDENTS
6163 	7 	  	LOAN BROKERS
6172 	7 	  	FINANCE LESSORS
6189 	OSF 	  	ASSET-BACKED SECURITIES
6199 	7 	  	FINANCE SERVICES
6200 	8 	  	SECURITY & COMMODITY BROKERS, DEALERS, EXCHANGES & SERVICES
6211 	12 	  	SECURITY BROKERS, DEALERS & FLOTATION COMPANIES
6221 	8 	  	COMMODITY CONTRACTS BROKERS & DEALERS
6282 	6 	  	INVESTMENT ADVICE
6311 	1 	  	LIFE INSURANCE
6321 	1 	  	ACCIDENT & HEALTH INSURANCE
6324 	1 	  	HOSPITAL & MEDICAL SERVICE PLANS
6331 	1 	  	FIRE, MARINE & CASUALTY INSURANCE
6351 	1 	  	SURETY INSURANCE
6361 	1 	  	TITLE INSURANCE
6399 	1 	  	INSURANCE CARRIERS, NEC
6411 	1 	  	INSURANCE AGENTS, BROKERS & SERVICE
6500 	8 	  	REAL ESTATE
6510 	8 	  	REAL ESTATE OPERATORS (NO DEVELOPERS) & LESSORS
6512 	8 	  	OPERATORS OF NONRESIDENTIAL BUILDINGS
6513 	8 	  	OPERATORS OF APARTMENT BUILDINGS
6519 	8 	  	LESSORS OF REAL PROPERTY, NEC
6531 	8 	  	REAL ESTATE AGENTS & MANAGERS (FOR OTHERS)
6532 	8 	  	REAL ESTATE DEALERS (FOR THEIR OWN ACCOUNT)
6552 	8 	  	LAND SUBDIVIDERS & DEVELOPERS (NO CEMETERIES)
6770 	All 	  	BLANK CHECKS
6792 	4 	  	OIL ROYALTY TRADERS
6794 	3 	  	PATENT OWNERS & LESSORS
6795 	9 	  	MINERAL ROYALTY TRADERS
6798 	8 	  	REAL ESTATE INVESTMENT TRUSTS
6799 	8 	  	INVESTORS, NEC
7000 	8 	  	HOTELS, ROOMING HOUSES, CAMPS & OTHER LODGING PLACES
7011 	8 	  	HOTELS & MOTELS
7200 	11 	  	SERVICES-PERSONAL SERVICES
7310 	11 	  	SERVICES-ADVERTISING
7311 	11 	  	SERVICES-ADVERTISING AGENCIES
7320 	11 	  	SERVICES-CONSUMER CREDIT REPORTING, COLLECTION AGENCIES
7330 	11 	  	SERVICES-MAILING, REPRODUCTION, COMMERCIAL ART & PHOTOGRAPHY
7331 	11 	  	SERVICES-DIRECT MAIL ADVERTISING SERVICES
7340 	8 	  	SERVICES-TO DWELLINGS & OTHER BUILDINGS
7350 	6 	  	SERVICES-MISCELLANEOUS EQUIPMENT RENTAL & LEASING
7359 	6 	  	SERVICES-EQUIPMENT RENTAL & LEASING, NEC
7361 	8 	  	SERVICES-EMPLOYMENT AGENCIES
7363 	11 	  	SERVICES-HELP SUPPLY SERVICES
7370 	3 	  	SERVICES-COMPUTER PROGRAMMING, DATA PROCESSING, ETC.
7371 	3 	  	SERVICES-COMPUTER PROGRAMMING SERVICES
7372 	3 	  	SERVICES-PREPACKAGED SOFTWARE
7373 	3 	  	SERVICES-COMPUTER INTEGRATED SYSTEMS DESIGN
7374 	3 	  	SERVICES-COMPUTER PROCESSING & DATA PREPARATION
7377 	3 	  	SERVICES-COMPUTER RENTAL & LEASING
7380 	11 	  	SERVICES-MISCELLANEOUS BUSINESS SERVICES
7381 	11 	  	SERVICES-DETECTIVE, GUARD & ARMORED CAR SERVICES
7384 	11 	  	SERVICES-PHOTOFINISHING LABORATORIES
7385 	11 	  	SERVICES-TELEPHONE INTERCONNECT SYSTEMS
7389 	2 & 3 	  	SERVICES-BUSINESS SERVICES, NEC
7500 	5 	  	SERVICES-AUTOMOTIVE REPAIR, SERVICES & PARKING
7510 	5 	  	SERVICES-AUTO RENTAL & LEASING (NO DRIVERS)
7600 	11 	  	SERVICES-MISCELLANEOUS REPAIR SERVICES
7812 	5 	  	SERVICES-MOTION PICTURE & VIDEO TAPE PRODUCTION
7819 	5 	  	SERVICES-ALLIED TO MOTION PICTURE PRODUCTION
7822 	5 	  	SERVICES-MOTION PICTURE & VIDEO TAPE DISTRIBUTION
7829 	5 	  	SERVICES-ALLIED TO MOTION PICTURE DISTRIBUTION
7830 	5 	  	SERVICES-MOTION PICTURE THEATERS
7841 	5 	  	SERVICES-VIDEO TAPE RENTAL
7900 	5 	  	SERVICES-AMUSEMENT & RECREATION SERVICES
7948 	5 	  	SERVICES-RACING, INCLUDING TRACK OPERATION
7990 	5 	  	SERVICES-MISCELLANEOUS AMUSEMENT & RECREATION
7997 	5 	  	SERVICES-MEMBERSHIP SPORTS & RECREATION CLUBS
8000 	9 	  	SERVICES-HEALTH SERVICES
8011 	1 	  	SERVICES-OFFICES & CLINICS OF DOCTORS OF MEDICINE
8050 	11 	  	SERVICES-NURSING & PERSONAL CARE FACILITIES
8051 	11 	  	SERVICES-SKILLED NURSING CARE FACILITIES
8060 	1 	  	SERVICES-HOSPITALS
8062 	1 	  	SERVICES-GENERAL MEDICAL & SURGICAL HOSPITALS, NEC
8071 	9 	  	SERVICES-MEDICAL LABORATORIES
8082 	9 	  	SERVICES-HOME HEALTH CARE SERVICES
8090 	9 	  	SERVICES-MISC HEALTH & ALLIED SERVICES, NEC
8093 	1 	  	SERVICES-SPECIALTY OUTPATIENT FACILITIES, NEC
8111 	11 	  	SERVICES-LEGAL SERVICES
8200 	11 	  	SERVICES-EDUCATIONAL SERVICES
8300 	9 	  	SERVICES-SOCIAL SERVICES
8351 	9 	  	SERVICES-CHILD DAY CARE SERVICES
8600 	5 	  	SERVICES-MEMBERSHIP ORGANIZATIONS
8700 	6 	  	SERVICES-ENGINEERING, ACCOUNTING, RESEARCH, MANAGEMENT
8711 	6 	  	SERVICES-ENGINEERING SERVICES
8731 	1 	  	SERVICES-COMMERCIAL PHYSICAL & BIOLOGICAL RESEARCH
8734 	9 	  	SERVICES-TESTING LABORATORIES
8741 	8 	  	SERVICES-MANAGEMENT SERVICES
8742 	8 	  	SERVICES-MANAGEMENT CONSULTING SERVICES
8744 	6 	  	SERVICES-FACILITIES SUPPORT MANAGEMENT SERVICES
8880 	99 	  	AMERICAN DEPOSITARY RECEIPTS
8888 	99 	  	FOREIGN GOVERNMENTS
8900 	11 	  	SERVICES-SERVICES, NEC
9721 	99 	  	INTERNATIONAL AFFAIRS
9995 	All 	  	NON-OPERATING ESTABLISHMENTS


*/