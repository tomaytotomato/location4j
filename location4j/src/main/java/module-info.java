module location4j {
  requires java.compiler;
  requires java.logging;

  exports com.tomaytotomato.location4j;
  exports com.tomaytotomato.location4j.aliases;
  exports com.tomaytotomato.location4j.loader;
  exports com.tomaytotomato.location4j.mapper;
  exports com.tomaytotomato.location4j.model;
  exports com.tomaytotomato.location4j.text.normaliser;
  exports com.tomaytotomato.location4j.text.tokeniser;
  exports com.tomaytotomato.location4j.usecase;
}