import groovy.sql.Sql

Sql.withInstance('jdbc:duckdb:', 'org.duckdb.DuckDBDriver') { sql ->
    def rows = sql.rows("""SELECT Country,
        rpad(string_agg(Rider, ', '), 40, ' ') as Riders,
        rpad(string_agg(Place, ', '), 10, ' ') as Places,
        bar(count(Country), 0, 4, 30) as Count
        FROM 'topten.csv' GROUP BY Country""")
    println "Country  Riders                                 Places    Count"
    rows.each { row -> row.with {
        println "$Country     $Riders $Places $Count"
    }}
}

/*
Country  Riders                                 Places    Count
🇩🇰     JONAS VINGEGAARD                         1          ███████▌
🇬🇧     ADAM YATES                               2          ███████▌
🇦🇺     BEN O'CONNOR, JAI HINDLEY, JACK HAIG     3, 4, 5    ██████████████████████▌
🇫🇷     GUILLAUME MARTIN, JULIAN ALAPHILIPPE     6, 10      ███████████████
🇿🇦     LOUIS DU BOUISSON MEINTJES               7          ███████▌
🇳🇴     TORSTEIN TRÆEN                           8          ███████▌
🇪🇸     CARLOS RODRIGUEZ CANO                    9          ███████▌
 */
