import groovy.sql.Sql
import java.util.stream.Collectors

Sql.withInstance('jdbc:duckdb:', 'org.duckdb.DuckDBDriver') { sql ->
    println "Country  Riders                                  Places    Count"
    sql.eachRow("""SELECT Country,
        rpad(string_agg(Rider, ', '), 40, ' ') as Riders,
        rpad(string_agg(Place, ', '), 10, ' ') as Places,
        bar(count(Country), 0, 4, 30) as Count
        FROM 'src/main/groovy/topten.csv' GROUP BY Country""") { row ->
        row.with {
            println "$Country       $Riders$Places$Count"
        }
    }
}

/*
Country  Riders                                  Places    Count
🇩🇰       JONAS VINGEGAARD                        1         ███████▌
🇬🇧       ADAM YATES                              2         ███████▌
🇦🇺       BEN O'CONNOR, JAI HINDLEY, JACK HAIG    3, 4, 5   ██████████████████████▌
🇫🇷       GUILLAUME MARTIN, JULIAN ALAPHILIPPE    6, 10     ███████████████
🇿🇦       LOUIS DU BOUISSON MEINTJES              7         ███████▌
🇳🇴       TORSTEIN TRÆEN                          8         ███████▌
🇪🇸       CARLOS RODRIGUEZ CANO                   9         ███████▌
*/

var f = 'src/main/groovy/topten.csv' as File
var lines = f.readLines()*.split(',')
var cols = lines[0].size()
var rows = lines[1..-1].collect{row ->
    (0..<cols).collectEntries{ col -> [lines[0][col], row[col]] }}
var commaDelimited = Collectors.joining(', ')
var aggRiders = { it.stream().map(rec -> rec.r.Rider).collect(commaDelimited) }
var aggPlaces = { it.stream().map(rec -> rec.r.Place).collect(commaDelimited) }
println GQ {
    from r in rows
    groupby r.Country
    select r.Country,
        agg(aggRiders(_g)) as Riders,
        agg(aggPlaces(_g)) as Places,
        '██' * count(r.Country) as Count
}

/*
+---------+--------------------------------------+---------+--------+
| Country | Riders                               | Places  | Count  |
+---------+--------------------------------------+---------+--------+
| 🇦🇺      | BEN O'CONNOR, JAI HINDLEY, JACK HAIG | 3, 4, 5 | ██████ |
| 🇩🇰      | JONAS VINGEGAARD                     | 1       | ██     |
| 🇳🇴      | TORSTEIN TRÆEN                       | 8       | ██     |
| 🇿🇦      | LOUIS DU BOUISSON MEINTJES           | 7       | ██     |
| 🇬🇧      | ADAM YATES                           | 2       | ██     |
| 🇪🇸      | CARLOS RODRIGUEZ CANO                | 9       | ██     |
| 🇫🇷      | GUILLAUME MARTIN, JULIAN ALAPHILIPPE | 6, 10   | ████   |
+---------+--------------------------------------+---------+--------+
*/
