import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static groovy.test.GroovyAssert.assertScript

class CaptainTest {
    @Test
    void makeCaptain2s() {
        assertScript '''
        import groovy.transform.builder.Builder
        import groovy.transform.builder.InitializerStrategy

        @Builder
        record Captain2(String first, String middle, String last) {
            @Builder(builderClassName="CaptainBuilderFull", builderMethodName="fullBuilder")
            Captain2(String full) {
                this(full.split(' ')[0], '', full.split(' ')[1])
            }
            @Builder(builderStrategy= InitializerStrategy)
            Captain2(String first, String last) {
                this(first, '', last)
            }
            String toString() {
                "$first ${middle? middle + ' ' : ''}$last"
            }
        }

        assert new Captain2('James', 'Tiberius', 'Kirk').toString() == 'James Tiberius Kirk'
        assert Captain2.fullBuilder().full('Jean-Luc Picard').build().toString() == 'Jean-Luc Picard'
        assert Captain2.builder().first('Christopher').last('Pike').build().toString() == 'Christopher Pike'
        assert new Captain2(Captain2.createInitializer().first('Kathryn').last('Janeway')).toString() == 'Kathryn Janeway'
        '''
    }

    @Test
    @CompileStatic
    void makeCaptains() {
//        assert new Captain('James', 'Tiberius', 'Kirk').toString() == 'James Tiberius Kirk'
//        assert Captain.fullBuilder().full('Jean-Luc Picard').build().toString() == 'Jean-Luc Picard'
//        assert Captain.builder().first('Christopher').last('Pike').build().toString() == 'Christopher Pike'
//        assert new Captain(Captain.createInitializer().first('Kathryn').last('Janeway')).toString() == 'Kathryn Janeway'
//        assert new Captain(Captain.createInitializer().first('Kathryn')).toString() == 'Kathryn Janeway'
    }
}
