package i2f.extension.agent.javassist.transformer;

import i2f.match.StringMatcher;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Ice2Faith
 * @date 2024/7/31 18:08
 * @desc
 */
public class XxeGuardClassTransformer implements ClassFileTransformer {
    public static void main(String[] args) {
        String className = "org.springframework.boot.web.servlet.context.WebApplicationContextServletContextAwareProcessor";
        boolean ret = StringMatcher.antClass().match(className, "org.springframework.**");
        System.out.println(ret);
    }



    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");

        if (!className.startsWith("javax.xml.")) {
            return null;
        }

        if ("javax.xml.parsers.DocumentBuilderFactory".equals(className)) {
            ClassPool cp = ClassPool.getDefault();
            System.out.println("xxe:" + className);
            CtClass cc = null;
            try {
                cc = cp.get(className);
                CtMethod method = cc.getDeclaredMethod("newInstance");

                method.insertAfter("{\n" +
                        "    if ($_ != null) { \n" +
                        "        javax.xml.parsers.DocumentBuilderFactory factory=(javax.xml.parsers.DocumentBuilderFactory)$_;" +
                        "        factory.setExpandEntityReferences(false); \n" +
                        "        System.out.println(\"disabled xxe features : DocumentBuilderFactory.newInstance()\"); \n" +
                        "    } \n" +
                        "}\n");
                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }

//            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//            builderFactory.setExpandEntityReferences(false);
//            System.out.println("disabled xxe features : DocumentBuilderFactory.newInstance()");
        }
        if ("javax.xml.parsers.SAXParserFactory".equals(className)) {
            ClassPool cp = ClassPool.getDefault();
            System.out.println("xxe:" + className);
            CtClass cc = null;
            try {
                cc = cp.get(className);
                CtMethod method = cc.getDeclaredMethod("newInstance");

                method.insertAfter("{\n" +
                        "    if ($_ != null) { \n" +
                        "         try {\n" +
                        "             $_.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true);\n" +
                        "             $_.setFeature(\"http://xml.org/sax/features/external-general-entities\", false);\n" +
                        "             $_.setFeature(\"http://xml.org/sax/features/external-parameter-entities\", false);\n" +
                        "             System.out.println(\"disabled xxe features : SAXParserFactory.newInstance()\");\n" +
                        "         } catch (Exception e) {\n" +
                        "             e.printStackTrace();\n" +
                        "         } \n" +
                        "    } \n" +
                        "}\n");

                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }

//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            try {
//                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
//                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
//                System.out.println("disabled xxe features : SAXParserFactory.newInstance()");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        if ("javax.xml.stream.XMLInputFactory".equals(className)) {
            ClassPool cp = ClassPool.getDefault();
            System.out.println("xxe:" + className);
            CtClass cc = null;
            try {
                cc = cp.get(className);
                CtMethod method = cc.getDeclaredMethod("newInstance");

                method.insertAfter("{\n" +
                        "    if ($_ != null) { \n" +
                        "        $_.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false); \n" +
                        "        $_.setProperty(XMLInputFactory.SUPPORT_DTD, false); \n" +
                        "        System.out.println(\"disabled xxe features : XMLInputFactory.newInstance()\"); \n" +
                        "    } \n" +
                        "}\n");

                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }

//            XMLInputFactory factory = XMLInputFactory.newInstance();
//            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
//            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
//            System.out.println("disabled xxe features : XMLInputFactory.newInstance()");
        }


        return null;
    }
}
