<h1>rabbit-in-your-headlights</h1>
<p>Frameworks/Technologies Used:</p>
<ul>
  <li>Spring Boot</li>
  <li>Spring AMQP</li>
  <li>Spring Social Twitter</li>
  <li>RabbitMQ</li>
  <li>Docker</li>
  <li>Lombok</li>
</ul>
<h2>Project Lombok</h2>
<ol>
  <li>Install Lombok Plugin</li>
  <li><strong>Enable annotation processing</strong> under Preferences -> Compiler -> Annotation Processors</li>
</ol>
<h2>RabbitMQ As A Service</h2>
<ul>
  <li>Start RabbitMQ Service (instructions for Windows <a href="https://www.rabbitmq.com/install-windows.html">here</a>)</li>
  <li>Access The HTTP API at http://127.0.0.1:15672</li>
</ul>
<h2>Twitter Template</h2>
The Application Defines A TwitterTemplate Bean Which Needs The <b>consumerKey, consumerSecret, accessToken,</b> and <b>accessTokenSecret</b> Property Values. These Are Related To The Twitter Application In Use (create one <a href="https://apps.twitter.com">here</a>), and since we can not commit them to GitHub, we chose to provide them as JVM parameters. Alternatively they can be defined as members of a .properties file.
