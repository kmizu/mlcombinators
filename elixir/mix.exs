defmodule ElxParser.Mixfile do
  use Mix.Project

  def project do
    [app: :elxparser,
     version: "0.0.1",
     elixir: "~> 1.0",
     description: "An Elixir Parser Combinator Library",
     package: package,
     deps: deps]
  end

  # Configuration for the OTP application
  #
  # Type "mix help compile.app" for more information
  def application do
    [applications: [:logger]]
  end

  defp deps do
    [{:ex_doc, ">= 0.0.0", only: :dev}]
  end

  defp package do
      [# These are the default files included in the package
        name: :elxparser,
        files: ["lib", "test", "mix.exs", "README*", "LICENSE*"],
        maintainers: ["Kota Mizushima"],
        licenses: ["MIT"],
        links: %{"GitHub" => "https://github.com/kmizu/elxparser"}]
  end
end
