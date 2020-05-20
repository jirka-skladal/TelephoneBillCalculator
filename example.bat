Run module as application:
java -jar target/billing-1.0-SNAPSHOT.jar "420774567453 13-01-2020 18:09:15 13-01-2020 18:12:57"


IntelliJ Idea RunConfiguration:
<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="MainApp" type="Application" factoryName="Application" nameIsGenerated="true">
    <option name="MAIN_CLASS_NAME" value="com.phonecompany.MainApp" />
    <module name="TelephoneBillCalculator" />
    <extension name="coverage">
      <pattern>
        <option name="PATTERN" value="com.phonecompany.*" />
        <option name="ENABLED" value="true" />
      </pattern>
    </extension>
    <method v="2">
      <option name="Make" enabled="true" />
    </method>
  </configuration>
</component>