# ElxParser

[![hex.pm version](https://img.shields.io/hexpm/v/elxparser.svg)](https://hex.pm/packages/elxparser)
[![hex.pm](https://img.shields.io/hexpm/l/elxparser.svg)](https://github.com/kmizu/elxparser/blob/master/LICENSE)

ElxParser is an experimental parser combinator library written in Elixir.
You can build your own parser using provided combinators.

## Installation

If [available in Hex](https://hex.pm/docs/publish), the package can be installed as:

  1. Add elxparser to your list of dependencies in `mix.exs`:

        def deps do
          [{:elxparser, "~> 0.0.1"}]
        end

  2. Ensure elxparser is started before your application:

        def application do
          [applications: [:elxparser]]
        end
